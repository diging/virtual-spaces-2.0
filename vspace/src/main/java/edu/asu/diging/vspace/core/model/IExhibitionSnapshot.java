package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface IExhibitionSnapshot extends IVSpaceElement {

    /**
     * Returns folder name of the Exhibition snapshot
     * @return
     */
    public String getFolderName();
    
    /**
     * Updates folder name of the Exhibition snapshot
     * @param folderName
     */
    public void setFolderName(String folderName);
    
    /**
     * Returns Snapshot task that tracks the status of exhibition folder snapshot.
     * @return
     */
    public SnapshotTask getSnapshotTask();
    
    /**
     * Updates Snapshot task that tracks the status of exhibition folder snapshot.
     * @param snapshotTask
     */
    public void setSnapshotTask(SnapshotTask snapshotTask);

}
