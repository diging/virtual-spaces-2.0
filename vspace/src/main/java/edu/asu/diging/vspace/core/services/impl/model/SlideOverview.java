package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

public class SlideOverview {

    private String name;

    private String id;

    private boolean isBranchingPoint;

    private List<String> sequenceIds;

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

    public boolean isBranchingPoint() {
        return isBranchingPoint;
    }

    public void setBranchingPoint(boolean isBranchingPoint) {
        this.isBranchingPoint = isBranchingPoint;
    }

    public List<String> getSequenceIds() {
        return sequenceIds;
    }

    public void setSequenceIds(List<String> sequenceIds) {
        this.sequenceIds = sequenceIds;
    }

}
