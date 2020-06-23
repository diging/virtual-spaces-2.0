package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.ChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class ChoiceBlockFactoryTest {
    
    private ChoiceBlockFactory choiceBlockFactoryToTest = new ChoiceBlockFactory();
    
    @Test
    public void test_createChoiceBlock_success() {
        
        Integer contentOrder = 1;
        
        Slide slide = new Slide();
        slide.setId("slide1"); 
        
        Choice choice = new Choice();
        choice.setId("choice1");  
        
        Choice choice2 = new Choice();
        choice.setId("choice2"); 
        
        List<IChoice> choiceList = new ArrayList();
        choiceList.add(choice);
        choiceList.add(choice2);
 
        IChoiceBlock actualChoiceBlock = choiceBlockFactoryToTest.createChoiceBlock(slide, contentOrder, choiceList);
        Assert.assertEquals(slide.getId(), actualChoiceBlock.getSlide().getId());
        Assert.assertEquals(contentOrder, actualChoiceBlock.getContentOrder());
        Assert.assertEquals(choice.getId(), actualChoiceBlock.getChoices().get(0).getId());
    }
}
