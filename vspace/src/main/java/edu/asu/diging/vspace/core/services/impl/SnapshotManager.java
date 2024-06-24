package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ExhibitionSnapshotRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IRenderingManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;

@Service
@PropertySource("classpath:app.properties")
public class SnapshotManager  implements  ISnapshotManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${page_size}")
    private int pageSize;
    
    @Value("${resources_folder_path}")
    private String resourcesPath;

    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;

    @Autowired
    private IRenderingManager renderingManager;

    private SequenceHistory sequenceHistory;

    @Autowired
    private ExhibitionSnapshotRepository exhibitionSnapshotRepository;

    @Autowired
    private SnapshotTaskRepository snapshotTaskRepository;
    
    @Autowired
    private SpaceRepository spaceRepository;
    
    private final String RESOURCES_FOLDER_NAME = "resources";

    /**
     * Triggers the creation of an exhibition snapshot.
     *
     * This method initializes and saves an {@link ExhibitionSnapshot} and a {@link SnapshotTask},
     *
     * @return the created {@link ExhibitionSnapshot}
     * @throws IOException                          if an I/O error occurs during the snapshot creation process
     * @throws InterruptedException                 if the snapshot creation process is interrupted
     * @throws SnapshotCouldNotBeCreatedException   if the snapshot could not be created due to any other errors
     */
    @Override
    @Transactional
    public ExhibitionSnapshot triggerExhibitionSnapshotCreation() throws IOException, InterruptedException, SnapshotCouldNotBeCreatedException {
        String exhibitionFolderName = getExhibitionFolderName();
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        createSnapshotFolder(exhibitionSnapshot, exhibitionFolderName);       
        SnapshotTask snapshotTask =  createSnapshotTask(exhibitionSnapshot);
        exhibitionSnapshot.setSnapshotTask(snapshotTask); 
        exhibitionSnapshotRepository.save(exhibitionSnapshot);

        try {
            createSnapshot(resourcesPath, exhibitionFolderName, sequenceHistory, exhibitionSnapshot);
            storageEngineDownloads.generateZip(exhibitionFolderName);
        } catch (IOException | InterruptedException | FileStorageException e) {
            throw new SnapshotCouldNotBeCreatedException(e.getMessage(), e);
        }
        return exhibitionSnapshot;
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
     * Creates a snapshot and copies the spaces to exhibitionFolderPath
     * 
     * @param resourcesPath - the path to the resources directory
     * @param exhibitionFolderName - the name of the folder where the exhibition data is stored
     * @param sequenceHistory - the history of sequences to be included in the snapshot
     * @param exhibitionSnapshot - the snapshot object that will store the exhibition state
     * @throws IOException - if an I/O error occurs during the snapshot creation
     * @throws InterruptedException - if the snapshot creation process is interrupted
     * @throws FileStorageException - if an error occurs while storing the snapshot
     * @throws ImageCouldNotBeStoredException 
     */    
    @Async
    @Override
    @Transactional
    public void createSnapshot(String resourcesPath, String exhibitionFolderName,SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException {
        storageEngineDownloads.copyToFolder(exhibitionFolderName + File.separator + RESOURCES_FOLDER_NAME, resourcesPath);
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            renderingManager.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);                
        }
        SnapshotTask snapshotTask = exhibitionSnapshot.getSnapshotTask();
        snapshotTask.setTaskComplete(true);
        snapshotTaskRepository.save(snapshotTask);   
    }

    /**
     * 
     * Creates folder for ExhibitionSnapshot and updates entity. 
     * 
     * @param exhibitionSnapshot     {@link ExhibitionSnapshot} for which the folder is created
     * @param exhibitionFolderName   the folder name of the snapshot
     * @return the name of the exhibition folder
     */
    private String createSnapshotFolder(ExhibitionSnapshot exhibitionSnapshot, String exhibitionFolderName) {
        storageEngineDownloads.createFolder(exhibitionFolderName);
        exhibitionSnapshot.setFolderName(exhibitionFolderName);
        exhibitionSnapshotRepository.save(exhibitionSnapshot); 
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

        if(exhibitionSnapshot.isPresent()) {           
            try {
                return storageEngineDownloads.getZip(exhibitionSnapshot.get().getFolderName());                
            }catch(FileSystemNotFoundException e) {
                throw new ExhibitionSnapshotNotFoundException(e.getMessage(), e);
            }
              
        } else {
            throw new ExhibitionSnapshotNotFoundException(id);
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

        Page<ExhibitionSnapshot> page =   exhibitionSnapshotRepository.findAllByOrderByCreationDateDesc(requestedPageForFiles);
        return page.map(exhibitionSnapshot-> { return (ExhibitionSnapshot) exhibitionSnapshot; } );
    }
    
    @Override
    public SnapshotTask getLatestSnapshotTask(){
        return snapshotTaskRepository.findFirstByOrderByCreationDateDesc();
    }
    
    @Override
    public SnapshotTask getSnapshotTask(String snapshotId) throws ExhibitionSnapshotNotFoundException{
        Optional<SnapshotTask> snapshotTask = snapshotTaskRepository.findByExhibitionSnapshotId(snapshotId);
        if(snapshotTask.isPresent()) {
            return snapshotTask.get();
        }
        else {
            throw new ExhibitionSnapshotNotFoundException(snapshotId);
        }
    }
}