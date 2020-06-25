package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;

@Entity
public class ChoiceBlock extends ContentBlock implements IChoiceBlock {

    @OneToMany(targetEntity = Choice.class, mappedBy = "choiceBlock", cascade=CascadeType.ALL)
    //@JoinTable(name="ChoiceBlock_Choice")
    private List<IChoice> choices;

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
}
