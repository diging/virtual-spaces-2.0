package edu.asu.diging.vspace.web.staff.forms;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;

public class SpaceForm {

	private String name;
	private String description;
	
    private List<LocalizedText> names = new ArrayList();
    
    private List<LocalizedText> descriptions =  new ArrayList();
	
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
    public List<LocalizedText> getNames() {
        return names;
    }
    public void setNames(List<LocalizedText> names) {
        this.names = names;
    }
    public List<LocalizedText> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LocalizedText> descriptions) {
        this.descriptions = descriptions;
    }
}
