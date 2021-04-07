package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.ChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;

public class ChoiceBlockFactoryTest {

    private ChoiceBlockFactory choiceBlockFactoryToTest = new ChoiceBlockFactory();

    @Test
    public void test_createChoiceBlock_success() {

        Integer contentOrder = 1;

        BranchingPoint slide = new BranchingPoint();
        slide.setId("slide1"); 

        Choice choice = new Choice();
        choice.setId("choice1");  

        Choice choice2 = new Choice();
        choice.setId("choice2"); 

        List<IChoice> choiceList = new ArrayList<IChoice>();
        choiceList.add(choice);
        choiceList.add(choice2);

        slide.setChoices(choiceList);

        IChoiceBlock actualChoiceBlock = choiceBlockFactoryToTest.createChoiceBlock(slide, contentOrder, choiceList, false);
        Assert.assertEquals(slide.getId(), actualChoiceBlock.getSlide().getId());
        Assert.assertEquals(contentOrder, actualChoiceBlock.getContentOrder());
        Assert.assertEquals(choice.getId(), actualChoiceBlock.getChoices().get(0).getId());

        IChoiceBlock actualChoiceBlockShowsAll = choiceBlockFactoryToTest.createChoiceBlock(slide, contentOrder, choiceList, true);
        Assert.assertEquals(slide.getId(), actualChoiceBlockShowsAll.getSlide().getId());
        Assert.assertEquals(contentOrder, actualChoiceBlockShowsAll.getContentOrder());
        Assert.assertEquals(choice.getId(), slide.getChoices().get(0).getId());
        Assert.assertNull(actualChoiceBlockShowsAll.getChoices());
    }
}
