package edu.asu.diging.vspace.core.model;

import java.util.List;

public class SequenceOverview{
 
    private String name;

    private String id;
    
    private List<ISlide> slides;

    public List<ISlide> getSlides() {
        return slides;
    }

    public void setSlides(List<ISlide> slides) {
        this.slides = slides;
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
