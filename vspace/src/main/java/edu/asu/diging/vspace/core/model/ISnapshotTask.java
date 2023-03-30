package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;

public interface ISnapshotTask  extends IVSpaceElement {

    
    public boolean isTaskComplete() ;
    
    public void setTaskComplete(boolean isTaskComplete);
    
    ExhibitionDownload getExhibitionDownload();
    
    void setExhibitionDownload(ExhibitionDownload exhibitionDownload) ;
    
}
