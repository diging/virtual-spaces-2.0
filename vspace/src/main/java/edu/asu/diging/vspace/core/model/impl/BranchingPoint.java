package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;

@Entity
@DiscriminatorValue("BranchingPoint")
public class BranchingPoint extends Slide implements IBranchingPoint {

    @ManyToMany(cascade=CascadeType.ALL, targetEntity = Choice.class)
    @JoinTable(name="BranchingPoint_Choices",
            joinColumns = @JoinColumn(name = "branchingPoint_id", referencedColumnName="id") ,
            inverseJoinColumns = @JoinColumn(name = "choice_id", referencedColumnName="id"))
    private List<IChoice> choices;
    
    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IBranchingPoint#getChoices()
     */
    @Override
    public List<IChoice> getChoices() {
        return choices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.model.impl.IBranchingPoint#setChoices(java.util.
     * List)
     */
    @Override
    public void setChoices(List<IChoice> choices) {
        this.choices = choices;
    }
}
