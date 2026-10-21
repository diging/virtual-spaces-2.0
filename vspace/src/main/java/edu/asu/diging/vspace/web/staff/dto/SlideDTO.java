package edu.asu.diging.vspace.web.staff.dto;

/**
 * Data Transfer Object for slide information in staff interface
 */
public class SlideDTO {
    private String id;
    private String name;
    private String description;
    
    public SlideDTO() {
    }
    
    public SlideDTO(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
}
