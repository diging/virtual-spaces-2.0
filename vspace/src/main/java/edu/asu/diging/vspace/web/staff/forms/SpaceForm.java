package edu.asu.diging.vspace.web.staff.forms;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;

public class SpaceForm {

	private String name;
	private String description;
	
    private List<LanguageDescriptionObject> names = new ArrayList();
    
    private List<LanguageDescriptionObject> descriptions =  new ArrayList();
	
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
    public List<LanguageDescriptionObject> getNames() {
        return names;
    }
    public void setNames(List<LanguageDescriptionObject> names) {
        this.names = names;
    }
    public List<LanguageDescriptionObject> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LanguageDescriptionObject> descriptions) {
        this.descriptions = descriptions;
    }
}
