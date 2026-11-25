package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISpace;

public class SpacesCustomOrderForm {
    
    private String name;
    private String description;
    
    private List<String> orderedSpaces;

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

    public List<String> getOrderedSpaces() {
        return orderedSpaces;
    }

    public void setOrderedSpaces(List<String> orderedSpaces) {
        this.orderedSpaces = orderedSpaces;
    }
    
    

}
