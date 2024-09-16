package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.asu.diging.vspace.core.model.ExhibitionModes;

public class ExhibitionConfigurationForm {
    
    private String customMessage;
    
    private String defaultExhibitLanguage;
    
    @NotEmpty(message = "Please select exhibition languages")
    private List<String> exhibitLanguage;
    
    @NotNull(message = "Please select exhibition mode")
    private ExhibitionModes exhibitionMode;
    
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

    public List<String> getExhibitLanguage() {
        return exhibitLanguage;
    }

    public void setExhibitLanguage(List<String> exhibitLanguage) {
        this.exhibitLanguage = exhibitLanguage;
    }

    public ExhibitionModes getExhibitionMode() {
        return exhibitionMode;
    }

    public void setExhibitionMode(ExhibitionModes exhibitionMode) {
        this.exhibitionMode = exhibitionMode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
