package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
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
    public IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder, List<IChoice> choices) {
        IChoiceBlock choiceBlock = new ChoiceBlock();
        List<IChoice> choicesList= new ArrayList<IChoice>();
        for(int i=0;i<choices.size();i++) {
            choicesList.add(choices.get(i));
        }
        choiceBlock.setSlide(slide);
        choiceBlock.setContentOrder(contentOrder);
        choiceBlock.setChoices(choicesList);
        return choiceBlock;
    }
}
