package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.ISlide;

/**
 * Creates a network to overview the current module within the exhibition.
 * @author prachikharge
 *
 */
public class ModuleOverview {
    
    private String name;
    private String id;
    private SequenceOverview startSequence;
    private List<SequenceOverview> otherSequences;
    
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
    
    public SequenceOverview getStartSequence() {
        return startSequence;
    }
    
    public void setStartSequence(SequenceOverview startSequence) {
        this.startSequence = startSequence;
    }
    
    public List<SequenceOverview> getOtherSequences() {
        return otherSequences;
    }
    public void setOtherSequences(List<SequenceOverview> otherSequences) {
        this.otherSequences = otherSequences;
    }
}