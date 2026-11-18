package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;

public interface ISnapshotTask  extends IVSpaceElement {

    
    public boolean isTaskComplete() ;
    
    public void setTaskComplete(boolean isTaskComplete);
    
    ExhibitionSnapshot getExhibitionSnapshot();
    
    void setExhibitionSnapshot(ExhibitionSnapshot exhibitionSnapshot) ;
    
}
