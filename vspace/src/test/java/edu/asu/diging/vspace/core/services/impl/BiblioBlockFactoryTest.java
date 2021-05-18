package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.factory.impl.BiblioBlockFactory;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class BiblioBlockFactoryTest {

    private BiblioBlockFactory biblioBlockFactoryToTest = new BiblioBlockFactory();
    
    @Mock
    private BiblioBlockRepository biblioBlockRepo;
    
    @Mock
    private SlideManager slideManager;

    @Test
    public void test_createBiblioBlock_success() {

        Integer contentOrder = 1;

        Slide slide = new Slide();
        slide.setId("slide1"); 

//        Choice choice = new Choice();
//        choice.setId("choice1");  
//
//        Choice choice2 = new Choice();
//        choice.setId("choice2"); 
//
//        List<IChoice> choiceList = new ArrayList<IChoice>();
//        choiceList.add(choice);
//        choiceList.add(choice2);
        
        IBiblioBlock biblioBlock = new BiblioBlock();
        biblioBlock.setTitle("TestTitle");
        biblioBlock.setAuthor("TestAuthor");
        biblioBlock.setYear(2000);
        biblioBlock.setJournal("TestJournal");
        biblioBlock.setUrl("TestUrl.com");
        biblioBlock.setVolume("TestVolume");
        biblioBlock.setIssue("TestIssue");
        biblioBlock.setPages("TestPages");
        biblioBlock.setEditors("TestEditors");
        biblioBlock.setType("TestType");
        biblioBlock.setNote("TestNote");
        biblioBlock.setContentOrder(contentOrder);

//        slide.setChoices(choiceList);

//        when(slideManager.getSlide(slide.getId())).thenReturn(slide);
        IBiblioBlock actualBiblioBlock = biblioBlockFactoryToTest.createBiblioBlock(slide, biblioBlock);
        Assert.assertEquals(slide.getId(), actualBiblioBlock.getSlide().getId());
        Assert.assertEquals(contentOrder, actualBiblioBlock.getContentOrder());
        Mockito.verify(biblioBlockRepo).save((BiblioBlock)biblioBlock);

    }
}
