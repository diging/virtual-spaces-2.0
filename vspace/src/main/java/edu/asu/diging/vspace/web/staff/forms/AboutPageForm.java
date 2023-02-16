package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;

public class AboutPageForm {
	
    private String title;

    private String aboutPageText;
    
    private List<LocalizedText> titles;
	
    private List<LocalizedText> aboutPageTexts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAboutPageText() {
        return aboutPageText;
    }

    public void setAboutPageText(String aboutPageText) {
        this.aboutPageText = aboutPageText;
    }

    public List<LocalizedText> getTitles() {
        return titles;
    }

    public void setTitles(List<LocalizedText> titles) {
        this.titles = titles;
    }

    public List<LocalizedText> getAboutPageTexts() {
        return aboutPageTexts;
    }

    public void setAboutPageTexts(List<LocalizedText> aboutPageTexts) {
        this.aboutPageTexts = aboutPageTexts;
    }	

}
