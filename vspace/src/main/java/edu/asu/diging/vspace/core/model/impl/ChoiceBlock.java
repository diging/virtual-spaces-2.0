package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;

import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISequence;

@Entity
public class ChoiceBlock extends ContentBlock implements IChoiceBlock {

    private ISequence sequence;

    @Override
    public ISequence getSequence() {
        return sequence;
    }

    @Override
    public void setSequence(ISequence sequence) {
        this.sequence = sequence;
    }
}
