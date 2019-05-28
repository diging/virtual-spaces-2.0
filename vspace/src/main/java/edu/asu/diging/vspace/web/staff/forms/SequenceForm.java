package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

public class SequenceForm {

    private String name;
    private String description;
    private List<ISlide> orderedSlides;
    
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
    public List<ISlide> getOrderedSlides() {
        return orderedSlides;
    }
    public void setOrderedSlides(List<ISlide> orderedSlides) {
        this.orderedSlides = orderedSlides;
    }
}