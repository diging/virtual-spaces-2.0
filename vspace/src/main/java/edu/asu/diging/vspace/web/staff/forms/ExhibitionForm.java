package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import edu.asu.diging.vspace.core.model.ExhibitionModes;

public class ExhibitionForm {
    
    @NotEmpty(message = "Please enter title")
    private String title;    
    @NotEmpty(message = "Please select exhibition languages")
    private List<String> exhibitLanguage;    
    @NotEmpty
    private ExhibitionModes exhibitMode;
    @NotEmpty
    private String spaceParam;
    
    private String exhibitionParam;
    private String customMessage;
    private String defaultExhibitLanguage;
    
    public String getCustomMessage() {
        return customMessage;
    }
    public String getDefaultExhibitLanguage() {
        return defaultExhibitLanguage;
    }
    public String getExhibitionParam() {
        return exhibitionParam;
    }
    public List<String> getExhibitLanguage() {
        return exhibitLanguage;
    }
    public ExhibitionModes getExhibitMode() {
        return exhibitMode;
    }
    public String getSpaceParam() {
        return spaceParam;
    }
    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
    public void setDefaultExhibitLanguage(String defaultExhibitLanguage) {
        this.defaultExhibitLanguage = defaultExhibitLanguage;
    }
    public void setExhibitionParam(String exhibitionParam) {
        this.exhibitionParam = exhibitionParam;
    }
    public void setExhibitLanguage(List<String> exhibitLanguage) {
        this.exhibitLanguage = exhibitLanguage;
    }
    public void setExhibitMode(ExhibitionModes exhibitMode) {
        this.exhibitMode = exhibitMode;
    }
    public void setSpaceParam(String spaceParam) {
        this.spaceParam = spaceParam;
    }  
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
