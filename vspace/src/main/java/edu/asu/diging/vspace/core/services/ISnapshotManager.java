package edu.asu.diging.vspace.core.services;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.model.ISnapshotTask;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface ISnapshotManager {
    ExhibitionSnapshot triggerExhibitionSnapshotCreation() throws IOException, InterruptedException, SnapshotCouldNotBeCreatedException, ExecutionException;
 
    byte[] getExhibitionSnapshot(String id) throws ExhibitionSnapshotNotFoundException, IOException, FileStorageException;

    Boolean isSnapshotCreated(String id);
    
    Page<ExhibitionSnapshot> getAllExhibitionSnapshots(int filesPagenum);

    ISnapshotTask getLatestSnapshotTask();

    ISnapshotTask getSnapshotTask(String id) throws ExhibitionSnapshotNotFoundException;
        
    ISnapshotTask createSnapshot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException, ExecutionException ;

}
