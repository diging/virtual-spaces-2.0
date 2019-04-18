package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

public class SequenceForm {

    private String name;
    private String description;
    private List<ISlide> slides;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<ISlide> getSlides() {
        return slides;
    }
    public void setSlides(List<ISlide> slides) {
        this.slides = slides;
    }
}