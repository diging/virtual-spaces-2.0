package edu.asu.diging.vspace.core.services.impl;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.BiblioBlockFactory;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class BiblioBlockFactoryTest {

    private BiblioBlockFactory biblioBlockFactoryToTest = new BiblioBlockFactory();

    @Test
    public void test_createBiblioBlock_success() {

        Integer contentOrder = 1;

        Slide slide = new Slide();
        slide.setId("slide1"); 

        IBiblioBlock biblioBlock = new BiblioBlock();
        biblioBlock.setTitle("TestTitle");
        biblioBlock.setAuthor("TestAuthor");
        biblioBlock.setYear("2000");
        biblioBlock.setJournal("TestJournal");
        biblioBlock.setUrl("TestUrl.com");
        biblioBlock.setVolume("TestVolume");
        biblioBlock.setIssue("TestIssue");
        biblioBlock.setPages("TestPages");
        biblioBlock.setEditors("TestEditors");
        biblioBlock.setType("TestType");
        biblioBlock.setNote("TestNote");
        biblioBlock.setContentOrder(contentOrder);

        IBiblioBlock actualBiblioBlock = biblioBlockFactoryToTest.createBiblioBlock(slide, biblioBlock);
        Assert.assertEquals(slide.getId(), actualBiblioBlock.getSlide().getId());
        Assert.assertEquals(contentOrder, actualBiblioBlock.getContentOrder());
    }
}
