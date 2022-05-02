package edu.asu.diging.vspace.core.services.impl.model;
import edu.asu.diging.vspace.core.model.impl.Slide;
public class SlideWithSpace extends Slide {
    
    private String spaceId;
    
    private String startSequenceId;
    
    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getStartSequenceId() {
        return startSequenceId;
    }

    public void setStartSequenceId(String startSequenceId) {
        this.startSequenceId = startSequenceId;
    }
    
}