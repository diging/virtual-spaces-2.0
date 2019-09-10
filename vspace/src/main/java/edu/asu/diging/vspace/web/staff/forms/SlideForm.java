package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

public class SlideForm {

    private String name;
    private String description;
    private String slideType;
    private List<String> choices;
    
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
    public String getSlideType() {
        return slideType;
    }
    public void setSlideType(String type) {
        this.slideType = type;
    }
    public List<String> getChoices() {
        return choices;
    }
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
