package edu.asu.diging.vspace.web.staff.forms;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;



public class SlideForm {

    private String name;
    private String description;
    private String type;
    private List<String> choices;
    
    private List<LocalizedTextForm> names = new ArrayList<LocalizedTextForm>();

    private List<LocalizedTextForm> descriptions = new ArrayList<LocalizedTextForm>();
    
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<String> getChoices() {
        return choices;
    }
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
    public List<LocalizedTextForm> getNames() {
        return names;
    }
    public void setNames(List<LocalizedTextForm> names) {
        this.names = names;
    }
    public List<LocalizedTextForm> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LocalizedTextForm> descriptions) {
        this.descriptions = descriptions;
    }
    
    
}
