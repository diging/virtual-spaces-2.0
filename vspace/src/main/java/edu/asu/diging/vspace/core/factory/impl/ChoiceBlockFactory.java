package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;

@Service
public class ChoiceBlockFactory implements IChoiceBlockFactory {
    
    @Override
    public IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder) {
        IChoiceBlock choiceBlock = new ChoiceBlock();
        choiceBlock.setSlide(slide);
        choiceBlock.setContentOrder(contentOrder);
        return choiceBlock;
    }
}
