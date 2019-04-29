package edu.asu.diging.vspace.web.staff.forms;

public class SequenceForm {

    private String name;
    private String description;
    private String[] orderedSlideIds;
    
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
    public String[] getOrderedSlideIds() {
        return orderedSlideIds;
    }
    public void setOrderedSlideIds(String[] orderedSlideIds) {
        this.orderedSlideIds = orderedSlideIds;
    }
}