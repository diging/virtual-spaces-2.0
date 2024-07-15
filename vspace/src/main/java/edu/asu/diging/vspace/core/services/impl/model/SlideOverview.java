package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

/**
 * Creates a network to overview the current slide within the exhibition.
 * @author prachikharge
 *
 */
public class SlideOverview {

    private String id;
    
    private String name;

    private boolean isBranchingPoint;

    private List<String> choiceSequenceNames;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBranchingPoint() {
        return isBranchingPoint;
    }

    public void setBranchingPoint(boolean isBranchingPoint) {
        this.isBranchingPoint = isBranchingPoint;
    }

    public List<String> getChoiceSequenceNames() {
        return choiceSequenceNames;
    }

    public void setChoiceSequenceNames(List<String> choiceSequenceNames) {
        this.choiceSequenceNames = choiceSequenceNames;
    }

}
