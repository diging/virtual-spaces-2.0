package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

public class ModuleOverview {
    
    private String name;
    private String id;
    private List<SequenceOverview> sequenceOverview;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<SequenceOverview> getSequenceOverview() {
        return sequenceOverview;
    }
    public void setSequenceOverview(List<SequenceOverview> sequenceOverview) {
        this.sequenceOverview = sequenceOverview;
    }

}