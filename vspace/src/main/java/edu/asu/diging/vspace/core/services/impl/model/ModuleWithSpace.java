package edu.asu.diging.vspace.core.services.impl.model;
import edu.asu.diging.vspace.core.model.impl.Module;

public class ModuleWithSpace extends Module {
    
    private String spaceId;
    
    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
    
}