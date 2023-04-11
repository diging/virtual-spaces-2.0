package edu.asu.diging.vspace.web.staff.forms;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;

public class AboutPageForm {
    
    private String title;

    private String aboutPageText;
    
    private List<LocalizedTextForm> titles = new ArrayList<LocalizedTextForm>();
	
    private List<LocalizedTextForm> aboutPageTexts = new ArrayList<LocalizedTextForm>();

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

    public List<LocalizedTextForm> getTitles() {
        return titles;
    }

    public void setTitles(List<LocalizedTextForm> titles) {
        this.titles = titles;
    }

    public List<LocalizedTextForm> getAboutPageTexts() {
        return aboutPageTexts;
    }

    public void setAboutPageTexts(List<LocalizedTextForm> aboutPageTexts) {
        this.aboutPageTexts = aboutPageTexts;
    }	

}
