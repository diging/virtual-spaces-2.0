package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;

public class AboutPageForm {
	
	private String title;

    private String aboutPageText;
    
	private List<LanguageDescriptionObject> titles;
	
	private List<LanguageDescriptionObject> aboutPageTexts;

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

	public List<LanguageDescriptionObject> getTitles() {
        return titles;
	}

	public void setTitles(List<LanguageDescriptionObject> titles) {
		this.titles = titles;
	}

	public List<LanguageDescriptionObject> getAboutPageTexts() {
		return aboutPageTexts;
	}

	public void setAboutPageTexts(List<LanguageDescriptionObject> aboutPageTexts) {
		this.aboutPageTexts = aboutPageTexts;
	}	

}
