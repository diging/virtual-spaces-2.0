package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;

@Entity
public class BranchingPoint extends Slide implements IBranchingPoint {

    @OneToMany(targetEntity = Choice.class)
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
