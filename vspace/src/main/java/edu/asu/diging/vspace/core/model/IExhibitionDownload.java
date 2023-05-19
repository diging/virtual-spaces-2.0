package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface IExhibitionDownload extends IVSpaceElement {

    /**
     * Returns folder name of the Exhibition download
     * @return
     */
    public String getFolderName();
    
    /**
     * Updates folder name of the Exhibition download
     * @param folderName
     */
    public void setFolderName(String folderName);
    
    /**
     * Returns Snapshot task that tracks the status of exhibition folder download.
     * @return
     */
    public SnapshotTask getSnapshotTask();
    
    /**
     * Updates Snapshot task that tracks the status of exhibition folder download.
     * @param snapshotTask
     */
    public void setSnapshotTask(SnapshotTask snapshotTask);

}
