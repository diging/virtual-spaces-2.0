package edu.asu.diging.vspace.core.services;

import java.io.IOException;
import java.util.concurrent.Future;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface IAsyncSnapshotCreator {

    /**
     * Creates a snapshot and copies the spaces to exhibitionFolderPath
     * 
     * @param resourcesPath - the path to the resources directory
     * @param exhibitionFolderName - the name of the folder where the exhibition data is stored
     * @param sequenceHistory - the history of sequences to be included in the snapshot
     * @param exhibitionSnapshot - the snapshot object that will store the exhibition state
     * @return Future containing the completed SnapshotTask
     * @throws IOException - if an I/O error occurs during the snapshot creation
     * @throws InterruptedException - if the snapshot creation process is interrupted
     * @throws FileStorageException - if an error occurs while storing the snapshot
     */   
    Future<SnapshotTask> createSnapshot(String resourcesPath, String exhibitionFolderName, 
            SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException;
}
