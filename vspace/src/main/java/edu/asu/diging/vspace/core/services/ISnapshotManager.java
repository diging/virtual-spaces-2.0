package edu.asu.diging.vspace.core.services;

import java.io.IOException;

import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;

import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface ISnapshotManager {
    ExhibitionSnapshot triggerExhibitionSnapshotCreation() throws IOException, InterruptedException, SnapshotCouldNotBeCreatedException;
 
    byte[] getExhibitionSnapshot(String id) throws ExhibitionSnapshotNotFoundException, IOException, FileStorageException;

    Boolean doesSnapshotExist(String id);

    String getExhibitionFolderName();

    Page<ExhibitionSnapshot> getAllExhibitionSnapshots(int filesPagenum);

    SnapshotTask getLatestSnapshotTask();

    SnapshotTask getSnapshotTask(String id) throws ExhibitionSnapshotNotFoundException;
        
    void createSnapshot(String resourcesPath, String exhibitionFolderName, SequenceHistory sequenceHistory, ExhibitionSnapshot exhibitionSnapshot) 
            throws IOException, InterruptedException, FileStorageException ;

}
