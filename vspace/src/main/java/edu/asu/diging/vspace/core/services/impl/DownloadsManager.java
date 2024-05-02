package edu.asu.diging.vspace.core.services.impl;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.services.IDownloadsManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;


@Service
public class DownloadsManager  implements  IDownloadsManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${page_size}")
    private int pageSize;

    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;

    @Autowired
    private ISnapshotManager snapshotManager;

    @Autowired
    private SequenceHistory sequenceHistory;

    @Autowired
    private ExhibitionDownloadRepository exhibitionDownloadRepository;

    @Autowired
    private SnapshotTaskRepository snapshotTaskRepository;


    /**
     * Triggers the snapshot creation process asynchronously. 
     * 
     * @param resourcesPath
     * @param exhibitionFolderName
     * @param context
     * @return
     * @throws IOException
     * @throws InterruptedException 
     * @throws ExecutionException 
     */

    @Override
    @Transactional
    public ExhibitionDownload triggerDownloadExhibition(String exhibitionFolderName) throws IOException, InterruptedException, ExecutionException {                 
        ExhibitionDownload exhibitionDownload = exhibitionDownloadRepository.findByFolderName(exhibitionFolderName);        
        if(exhibitionDownload == null ) {
            exhibitionDownload = new ExhibitionDownload();
        }
        createFolderAndUpdateExhibitionDownload(exhibitionDownload, exhibitionFolderName);

        SnapshotTask snapshotTask =  createSnapshotTask(exhibitionDownload);

        exhibitionDownload.setSnapshotTask(snapshotTask); 
        exhibitionDownloadRepository.save(exhibitionDownload);

        try {
            snapshotManager.createSnapshot(exhibitionFolderName, sequenceHistory, exhibitionDownload);
        } catch (IOException | InterruptedException | FileStorageException e) {
            logger.error("Could not create a snapshot",e);
        }
        return exhibitionDownload;
    }

    /**
     * Creates and saves snapshotTask object.
     * @param exhibitionDownload
     * @return
     */
    private SnapshotTask createSnapshotTask(ExhibitionDownload exhibitionDownload) {
        SnapshotTask snapshotTask = new SnapshotTask();  
        snapshotTask.setExhibitionDownload(exhibitionDownload);        
        snapshotTaskRepository.save(snapshotTask);
        return snapshotTask;
    }

    /**
     * 
     * Creates folder for ExhibitionDownload and updates entity. 
     * @param exhibitionDownload
     * @param exhibitionFolderName
     * @return
     */
    private String createFolderAndUpdateExhibitionDownload(ExhibitionDownload exhibitionDownload, String exhibitionFolderName) {
        storageEngineDownloads.createFolder(exhibitionFolderName);
        exhibitionDownload.setFolderName(exhibitionFolderName);
        exhibitionDownloadRepository.save(exhibitionDownload); 
        return exhibitionFolderName;
    }


    /**
     * Downloads the given exhibition folder by the given ExhibitionDownload id
     * 
     * @param id
     * @return
     * @throws ExhibitionDownloadNotFoundException
     * @throws IOException
     */
    @Override
    public byte[] downloadExhibitionFolder(String id) throws ExhibitionDownloadNotFoundException, IOException {
        Optional<ExhibitionDownload> exhibitionDownlaod = exhibitionDownloadRepository.findById(id);

        if(exhibitionDownlaod.isPresent()) {
            
            try {
                return storageEngineDownloads.generateZipFolder(exhibitionDownlaod.get().getFolderName());                
            }catch(FileSystemNotFoundException e) {
                logger.error("Zip folder not yet created", e);
                throw new ExhibitionDownloadNotFoundException(id);
            }
              
        } else {
            throw new ExhibitionDownloadNotFoundException(id);
        }
    }
 


    /**
     * Return true is corresponding snapshot is created. Else return false.
     */
    @Override
    @Transactional
    public Boolean checkIfSnapshotCreated(String id) {
        Optional<ExhibitionDownload> exhibitionDownload = exhibitionDownloadRepository.findById(id);
        if(exhibitionDownload.isPresent()) {
            return exhibitionDownload.get().getSnapshotTask().isTaskComplete();            
        }
        return false;
    }

    @Override
    public String getExhibitionFolderName() {
        String folderName = "Exhibition"+ LocalDateTime.now().toString();
        return folderName.replace( ":" , "." )+"Z";
    }

    @Override
    public Page<ExhibitionDownload> getAllExhibitionDownloads(int filesPagenum) {

        if (filesPagenum < 1) {
            filesPagenum = 1;
        }
        Pageable requestedPageForFiles = PageRequest.of(filesPagenum - 1, pageSize);

        Page<ExhibitionDownload> page =   exhibitionDownloadRepository.findAllByOrderByCreationDateDesc(requestedPageForFiles);
        return page.map(exhibitionDownload-> { return (ExhibitionDownload) exhibitionDownload; } );
    }

}