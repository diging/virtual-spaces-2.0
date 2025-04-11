package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IRenderingManager;

@Service
public class AsyncSnapshotCreator {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final String RESOURCES_FOLDER_NAME = "resources";
    
    @Autowired
    private SpaceRepository spaceRepository;
    
    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;
    
    @Autowired
    private IRenderingManager renderingManager;
    
    @Autowired
    private SnapshotTaskRepository snapshotTaskRepository;   

    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath
     * 
     * @param resourcesPath - the path to the resources directory
     * @param exhibitionFolderName - the name of the folder where the exhibition data is stored
     * @param sequenceHistory - the history of sequences to be included in the snapshot
     * @param exhibitionSnapshot - the snapshot object that will store the exhibition state
     * @return 
     * @throws IOException - if an I/O error occurs during the snapshot creation
     * @throws InterruptedException - if the snapshot creation process is interrupted
     * @throws FileStorageException - if an error occurs while storing the snapshot
     * @throws ImageCouldNotBeStoredException 
     */   
    @Async
    @Transactional
    public Future<SnapshotTask> createSnapshot(String resourcesPath, String exhibitionFolderName,SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException {
        storageEngineDownloads.copyToFolder(exhibitionFolderName + File.separator + RESOURCES_FOLDER_NAME, resourcesPath);
        List<Space> spaces= spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED);

        for(Space space : spaces) {
            renderingManager.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);                
        }
        SnapshotTask snapshotTask = exhibitionSnapshot.getSnapshotTask();
        snapshotTask.setTaskComplete(true);
        return new AsyncResult<SnapshotTask>(snapshotTaskRepository.save(snapshotTask));   
    }
}
