package edu.asu.diging.vspace.web.staff.forms;

public class LocalizedTextForm {
    
    private String text;
    
    private String localizedTextId;
    
    private String exhibitionLanguageId;
    
    private String exhibitionLanguageLabel;  
    
    private Boolean isDefaultExhibitionLanguage;


    public LocalizedTextForm() {
        super();
    }

    public LocalizedTextForm(String text, String localizedTextId, String exhibitionLanguageId,
            String exhibitionLanguageLabel) {
        super();
        this.text = text;
        this.localizedTextId = localizedTextId;
        this.exhibitionLanguageId = exhibitionLanguageId;
        this.exhibitionLanguageLabel = exhibitionLanguageLabel;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocalizedTextId() {
        return localizedTextId;
    }

    public void setLocalizedTextId(String localisedTextId) {
        this.localizedTextId = localisedTextId;
    }

    public String getExhibitionLanguageId() {
        return exhibitionLanguageId;
    }

    public void setExhibitionLanguageId(String exhibitionLanguageId) {
        this.exhibitionLanguageId = exhibitionLanguageId;
    }

    public String getExhibitionLanguageLabel() {
        return exhibitionLanguageLabel;
    }

    public void setExhibitionLanguageLabel(String exhibitionLanguageLabel) {
        this.exhibitionLanguageLabel = exhibitionLanguageLabel;
    }

    public Boolean getIsDefaultExhibitionLanguage() {
        return isDefaultExhibitionLanguage;
    }

    public void setIsDefaultExhibitionLanguage(Boolean isDefaultExhibitionLanguage) {
        this.isDefaultExhibitionLanguage = isDefaultExhibitionLanguage;
    }
}

