package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ExhibitionSnapshotRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.NoPublishedSpacesException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IRenderingManager;
import edu.asu.diging.vspace.core.services.IAsyncSnapshotCreator;
import edu.asu.diging.vspace.core.services.ISnapshotManager;

@Service
@PropertySource("classpath:app.properties")
public class SnapshotManager  implements  ISnapshotManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${page_size}")
    private int pageSize;
    
    @Autowired
    private ServletContext servletContext;

    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;

    @Autowired
    private ExhibitionSnapshotRepository exhibitionSnapshotRepository;

    @Autowired
    private SnapshotTaskRepository snapshotTaskRepository;

    @Autowired
    private SpaceRepository spaceRepository;
    
    @Autowired
    private IAsyncSnapshotCreator asyncSnapshotCreator;
    
    private final String ZIP_FILE_EXTENSION = ".zip";

    /**
     * Triggers the creation of an exhibition snapshot.
     * This method initializes and saves an {@link ExhibitionSnapshot} and a {@link SnapshotTask}.
     * If the snapshot cannot be created, the partially written folder is removed and
     * the snapshot and its task are rolled back, so no unfinished snapshot is left behind.
     *
     * @return the created {@link ExhibitionSnapshot}
     * @throws IOException if an I/O error occurs during the snapshot creation process
     * @throws InterruptedException if the snapshot creation process is interrupted
     * @throws NoPublishedSpacesException if there are no published spaces to export
     * @throws SnapshotCouldNotBeCreatedException if the snapshot could not be created due to any other errors
     * @throws ExecutionException if the async task execution fails
     */
    @Override
    @Transactional(rollbackFor = SnapshotCouldNotBeCreatedException.class)
    public ExhibitionSnapshot triggerExhibitionSnapshotCreation() throws IOException, InterruptedException, SnapshotCouldNotBeCreatedException, ExecutionException {
        if (spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED).isEmpty()) {
            throw new NoPublishedSpacesException();
        }

        String exhibitionFolderName = getExhibitionFolderName();
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        createSnapshotFolder(exhibitionSnapshot, exhibitionFolderName);       
        SnapshotTask snapshotTask = createSnapshotTask(exhibitionSnapshot);
        exhibitionSnapshot.setSnapshotTask(snapshotTask); 
        exhibitionSnapshot = exhibitionSnapshotRepository.save(exhibitionSnapshot);

        try {
            snapshotTask = createSnapshot(getResourcesPath(), exhibitionFolderName, exhibitionSnapshot);
            storageEngineDownloads.generateZip(exhibitionFolderName);
        } catch (IOException | InterruptedException | FileStorageException | ExecutionException | RuntimeException e) {
            deleteSnapshotFiles(exhibitionFolderName);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            Throwable cause = e instanceof ExecutionException && e.getCause() != null ? e.getCause() : e;
            throw new SnapshotCouldNotBeCreatedException(cause.getMessage(), cause);
        }
        return exhibitionSnapshot;
    }

    /**
     * Returns the path of the static resources (css, js, fonts) of the deployed application,
     * which are copied into every snapshot.
     *
     * @return the absolute path of the resources folder
     * @throws IOException if the resources folder cannot be found
     */
    private String getResourcesPath() throws IOException {
        String resourcesPath = servletContext.getRealPath("/resources");
        if (resourcesPath == null || !new File(resourcesPath).isDirectory()) {
            throw new IOException("Could not find the resources folder of the application.");
        }
        return resourcesPath;
    }

    /**
     * Removes the folder and zip file of a snapshot that could not be created.
     *
     * @param exhibitionFolderName the folder name of the snapshot
     */
    private void deleteSnapshotFiles(String exhibitionFolderName) {
        try {
            storageEngineDownloads.deleteFolder("", exhibitionFolderName);
        } catch (IOException e) {
            logger.error("Could not delete folder of failed snapshot " + exhibitionFolderName, e);
        }
        storageEngineDownloads.getFile("", exhibitionFolderName + ZIP_FILE_EXTENSION).delete();
    }

    /**
     * Creates and saves snapshotTask object.
     * 
     * @param exhibitionSnapshot {@link ExhibitionSnapshot} object for which the task is to be created
     * @return the created       {@link SnapshotTask}
     */
    private SnapshotTask createSnapshotTask(ExhibitionSnapshot exhibitionSnapshot) {
        SnapshotTask snapshotTask = new SnapshotTask();  
        snapshotTask.setExhibitionSnapshot(exhibitionSnapshot);      
        return snapshotTaskRepository.save(snapshotTask);
    }
    
    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath.
     * 
     * @param resourcesPath the path to the resources directory
     * @param exhibitionFolderName the name of the folder where the exhibition data is stored
     * @param exhibitionSnapshot the snapshot object that will store the exhibition state
     * @return the completed SnapshotTask
     * @throws IOException if an I/O error occurs during the snapshot creation
     * @throws InterruptedException if the snapshot creation process is interrupted
     * @throws FileStorageException if an error occurs while storing the snapshot
     * @throws ExecutionException if the async task execution fails
     */   
    @Override
    @Transactional
    public SnapshotTask createSnapshot(String resourcesPath, String exhibitionFolderName, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException, ExecutionException {
        Future<SnapshotTask> futureTask = asyncSnapshotCreator.createSnapshot(resourcesPath, exhibitionFolderName, exhibitionSnapshot);
        return futureTask.get();
    }

    /**
     * 
     * Creates folder for ExhibitionSnapshot. 
     * 
     * @param exhibitionSnapshot     {@link ExhibitionSnapshot} for which the folder is created
     * @param exhibitionFolderName   the folder name of the snapshot
     * @return the name of the exhibition folder
     */
    private String createSnapshotFolder(ExhibitionSnapshot exhibitionSnapshot, String exhibitionFolderName) {
        exhibitionFolderName = storageEngineDownloads.createFolder(exhibitionFolderName);
        exhibitionSnapshot.setFolderName(exhibitionFolderName);
        exhibitionSnapshot = exhibitionSnapshotRepository.save(exhibitionSnapshot);
        
        return exhibitionFolderName; 
    }

    /**
     * Downloads the given exhibition folder by the given ExhibitionSnapshot id
     * 
     * @param id  - exhibition snapshot id
     * @return - The zip file as a byte array
     * @throws ExhibitionDownloadNotFoundException
     * @throws IOException
     */
    @Override
    public byte[] getExhibitionSnapshot(String id) throws ExhibitionSnapshotNotFoundException, IOException {
        Optional<ExhibitionSnapshot> exhibitionSnapshot = exhibitionSnapshotRepository.findById(id);

        if (!exhibitionSnapshot.isPresent()) {           
            throw new ExhibitionSnapshotNotFoundException("Exhibition Snapshot not found");             
        }
        try {
            return storageEngineDownloads.getMediaContent("", exhibitionSnapshot.get().getFolderName() + ZIP_FILE_EXTENSION);
        } catch (IOException e) {
            throw new ExhibitionSnapshotNotFoundException("Failed to retrieve media content for exhibition snapshot", e);
        }
    }

    /**
     * Return true if corresponding snapshot is created. Else return false.
     */
    @Override
    @Transactional
    public Boolean isSnapshotCreated(String id) {
        return exhibitionSnapshotRepository.findById(id).isPresent();
    }

    private String getExhibitionFolderName() {
        return "Exhibition"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss"));
    }

    @Override
    public Page<ExhibitionSnapshot> getAllExhibitionSnapshots(int filesPagenum) {
        if (filesPagenum < 1) {
            filesPagenum = 1;
        }

        Pageable requestedPageForFiles = PageRequest.of(filesPagenum - 1, pageSize);
        return exhibitionSnapshotRepository.findAllByOrderByCreationDateDesc(requestedPageForFiles);
    }
    
    @Override
    public SnapshotTask getLatestSnapshotTask(){
        return snapshotTaskRepository.findFirstByOrderByCreationDateDesc();
    }
    
    @Override
    public SnapshotTask getSnapshotTask(String snapshotId) throws ExhibitionSnapshotNotFoundException {
        Optional<SnapshotTask> snapshotTask = snapshotTaskRepository.findByExhibitionSnapshotId(snapshotId);
        if (snapshotTask.isPresent()) {
            return snapshotTask.get();
        } else {
            throw new ExhibitionSnapshotNotFoundException(snapshotId);
        }
    }
}