package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;

@Entity
public class ChoiceBlock extends ContentBlock implements IChoiceBlock {

    @OneToOne(targetEntity = Choice.class)
    private IChoice choice;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#getSequence()
     */
    @Override
    public IChoice getChoice() {
        return choice;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#setSequence(edu.asu.diging.vspace.core.model.impl.ISequence)
     */
    @Override
    public void setChoice(IChoice choice) {
        this.choice = choice;
    }
}
