package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;

@Service
public class ChoiceBlockFactory implements IChoiceBlockFactory {

    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.factory.impl.IChoiceBlockFactory#createChoiceBlock(edu.asu.diging.vspace.core.model.impl.ISlide,
     * edu.asu.diging.vspace.core.model.impl.IChoice)
     */
    @Override
    public IChoiceBlock createChoiceBlock(ISlide slide, List<IChoice> choices, boolean showsAll) {
        IChoiceBlock choiceBlock = new ChoiceBlock();
        choiceBlock.setSlide(slide);
        choiceBlock.setShowsAll(showsAll);
        if(!showsAll) {
            choiceBlock.setChoices(choices);
        }
        return choiceBlock;
    }
}
