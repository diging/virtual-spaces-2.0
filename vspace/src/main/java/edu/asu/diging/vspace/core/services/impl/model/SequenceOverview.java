package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

public class SequenceOverview {
 
    private String name;

    private String id;
    
    private List<SlideOverview> slideOverview;

    public List<SlideOverview> getSlides() {
        return slideOverview;
    }

    public void setSlides(List<SlideOverview> slideOverview) {
        this.slideOverview = slideOverview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}
