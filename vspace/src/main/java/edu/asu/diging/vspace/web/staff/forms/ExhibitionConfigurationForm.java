package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.asu.diging.vspace.core.model.ExhibitionModes;

public class ExhibitionConfigurationForm {
    
    private String customMessage;
    
    private String defaultExhibitLanguage;    
    
    private String exhibitionParam;
    
    @NotEmpty(message = "Please select exhibition languages")
    private List<String> exhibitLanguage;
    
    @NotNull(message = "Please select exhibit mode")
    private ExhibitionModes exhibitMode;
    
    @NotEmpty
    private String spaceParam;
    
    @NotEmpty(message = "Please enter title")
    private String title;
    
    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public String getDefaultExhibitLanguage() {
        return defaultExhibitLanguage;
    }

    public void setDefaultExhibitLanguage(String defaultExhibitLanguage) {
        this.defaultExhibitLanguage = defaultExhibitLanguage;
    }

    public String getExhibitionParam() {
        return exhibitionParam;
    }

    public void setExhibitionParam(String exhibitionParam) {
        this.exhibitionParam = exhibitionParam;
    }

    public List<String> getExhibitLanguage() {
        return exhibitLanguage;
    }

    public void setExhibitLanguage(List<String> exhibitLanguage) {
        this.exhibitLanguage = exhibitLanguage;
    }

    public ExhibitionModes getExhibitMode() {
        return exhibitMode;
    }

    public void setExhibitMode(ExhibitionModes exhibitMode) {
        this.exhibitMode = exhibitMode;
    }

    public String getSpaceParam() {
        return spaceParam;
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
