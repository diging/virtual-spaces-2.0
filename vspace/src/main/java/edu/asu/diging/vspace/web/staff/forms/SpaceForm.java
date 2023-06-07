package edu.asu.diging.vspace.web.staff.forms;

import java.util.ArrayList;
import java.util.List;

import edu.asu.diging.vspace.core.model.impl.LocalizedText;

public class SpaceForm {

	private String name;
	private String description;
	
	private LocalizedTextForm defaultName;

    private LocalizedTextForm defaultDescription;
	
    private List<LocalizedTextForm> names = new ArrayList<LocalizedTextForm>();
    
    private List<LocalizedTextForm> descriptions =  new ArrayList<LocalizedTextForm>();
	
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
    public List<LocalizedTextForm> getNames() {
        return names;
    }
    public void setNames(List<LocalizedTextForm> names) {
        this.names = names;
    }
    public List<LocalizedTextForm> getDescriptions() {
        return descriptions;
    }
    public void setDescriptions(List<LocalizedTextForm> descriptions) {
        this.descriptions = descriptions;
    }
    public LocalizedTextForm getDefaultName() {
        return defaultName;
    }
    public void setDefaultName(LocalizedTextForm defaultName) {
        this.defaultName = defaultName;
    }
    public LocalizedTextForm getDefaultDescription() {
        return defaultDescription;
    }
    public void setDefaultDescription(LocalizedTextForm defaultDescription) {
        this.defaultDescription = defaultDescription;
    }
    
    
}
