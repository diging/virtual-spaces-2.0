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
     * @see edu.asu.diging.vspace.core.factory.impl.IChoiceBlockFactory#createChoiceBlock(edu.asu.diging.vspace.core.model.impl.ISlide, java.lang.Integer,
     * edu.asu.diging.vspace.core.model.impl.IChoice)
     */    
    @Override
    public IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder, List<IChoice> choices, boolean showsAll) {
        IChoiceBlock choiceBlock = new ChoiceBlock();
        choiceBlock.setSlide(slide);
        choiceBlock.setContentOrder(contentOrder);
        if(showsAll) {
            choiceBlock.setShowsAll(showsAll);
        }
        else {
            choiceBlock.setChoices(choices);
            choiceBlock.setShowsAll(showsAll);
        }
        return choiceBlock;
    }
}
