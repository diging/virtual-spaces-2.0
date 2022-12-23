package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.SlideExhibitionLanguageObject;

public class SlideForm {

    private String name;
    private String description;
    private String type;
    private List<String> choices;
    
    private List<SlideExhibitionLanguageObject> names;

    private List<SlideExhibitionLanguageObject> descriptions;
    
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
    public List<SlideExhibitionLanguageObject> getNames() {
        return names;
    }
    public void setNames(List<SlideExhibitionLanguageObject> names) {
        this.names = names;
    }
    public List<SlideExhibitionLanguageObject> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<SlideExhibitionLanguageObject> descriptions) {
        this.descriptions = descriptions;
    }
    
    
}
