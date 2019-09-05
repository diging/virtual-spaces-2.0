package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

public class SequenceForm {

    private String name;
    private String description;
    private List<String> orderedSlides;
    
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
    public List<String> getOrderedSlides() {
        return orderedSlides;
    }
    public void setOrderedSlides(List<String> orderedSlides) {
        this.orderedSlides = orderedSlides;
    }
}