package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

public class SlideForm extends LocalizedNameDescriptionForm {

    private String type;
    private List<String> choices;

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
}
