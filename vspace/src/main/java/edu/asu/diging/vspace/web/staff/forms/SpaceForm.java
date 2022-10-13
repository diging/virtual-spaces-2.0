package edu.asu.diging.vspace.web.staff.forms;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LanguageObject;

public class SpaceForm {

	private String name;
	private String description;
	
	private List<LanguageObject> names;
	
	private List<LanguageObject> descriptions;
	
	
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
    public List<LanguageObject> getNames() {
        return names;
    }
    public void setNames(List<LanguageObject> names) {
        this.names = names;
    }
    public List<LanguageObject> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LanguageObject> descriptions) {
        this.descriptions = descriptions;
    }
}
