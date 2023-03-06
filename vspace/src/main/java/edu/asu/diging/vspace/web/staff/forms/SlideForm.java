package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;



public class SlideForm {

    private String name;
    private String description;
    private String type;
    private List<String> choices;
    
    private List<LocalizedText> names;

    private List<LocalizedText> descriptions;
    
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
    public List<LocalizedText> getNames() {
        return names;
    }
    public void setNames(List<LocalizedText> names) {
        this.names = names;
    }
    public List<LocalizedText> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LocalizedText> descriptions) {
        this.descriptions = descriptions;
    }
    
    
}
