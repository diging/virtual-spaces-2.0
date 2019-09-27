package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISequence;

@Entity
public class ChoiceBlock extends ContentBlock implements IChoiceBlock {

    @OneToOne(targetEntity = Sequence.class)
    private ISequence sequence;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.ISequence#getSequence()
     */
    @Override
    public ISequence getSequence() {
        return sequence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#setSequence(edu.asu.diging.vspace.core.model.impl.ISequence)
     */
    @Override
    public void setSequence(ISequence sequence) {
        this.sequence = sequence;
    }
}
