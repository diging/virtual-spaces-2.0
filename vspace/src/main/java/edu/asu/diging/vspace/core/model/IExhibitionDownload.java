package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.SnapshotTask;

public interface IExhibitionDownload extends IVSpaceElement {

    public String getFolderPath();
    public void setFolderPath(String folderPath);
    public String getFolderName();
    public void setFolderName(String folderName) ;
    public SnapshotTask getSnapshotTask() ;
    public void setSnapshotTask(SnapshotTask snapshotTask);

}
