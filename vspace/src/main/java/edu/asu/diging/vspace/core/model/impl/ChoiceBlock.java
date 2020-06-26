package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;

@Entity
public class ChoiceBlock extends ContentBlock implements IChoiceBlock {

    @ManyToMany(targetEntity = Choice.class)
    private List<IChoice> choices;

    private boolean showsAll;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#getChoices()
     */
    @Override
    public List<IChoice> getChoices() {
        return choices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.model.impl.IChoice#setChoices(edu.asu.diging.vspace.core.model.impl.IChoice)
     */
    @Override
    public void setChoices(List<IChoice> choices) {
        this.choices = choices;
    }

    @Override
    public boolean isShowsAll() {
        return showsAll;
    }

    @Override
    public void setShowsAll(boolean showsAll) {
        this.showsAll = showsAll;
    }
}
