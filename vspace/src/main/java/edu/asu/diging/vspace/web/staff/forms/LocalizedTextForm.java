package edu.asu.diging.vspace.web.staff.forms;

public class LocalizedTextForm {

    String text;

    String localisedTextId;

    String exhibitionLanguageId;

    String exhibitionLanguageLabel;  

    Boolean isDefaultExhibitionLanguage;

    public Boolean getIsDefaultExhibitionLanguage() {
        return isDefaultExhibitionLanguage;
    }

    public void setIsDefaultExhibitionLanguage(Boolean isDefaultExhibitionLanguage) {
        this.isDefaultExhibitionLanguage = isDefaultExhibitionLanguage;
    }

    public LocalizedTextForm() {
        super();
    }

    public LocalizedTextForm(String text, String localisedTextId, String exhibitionLanguageId,
            String exhibitionLanguageLabel) {
        super();
        this.text = text;
        this.localisedTextId = localisedTextId;
        this.exhibitionLanguageId = exhibitionLanguageId;
        this.exhibitionLanguageLabel = exhibitionLanguageLabel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocalisedTextId() {
        return localisedTextId;
    }

    public void setLocalisedTextId(String localisedTextId) {
        this.localisedTextId = localisedTextId;
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

}