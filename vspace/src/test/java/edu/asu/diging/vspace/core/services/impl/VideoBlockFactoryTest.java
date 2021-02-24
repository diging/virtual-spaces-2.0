package edu.asu.diging.vspace.core.services.impl;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.VideoBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSVideo;

public class VideoBlockFactoryTest {

    private VideoBlockFactory videoBlockFactory = new VideoBlockFactory();
    
    
    @Test
    public void test_createVideoBlock_success() {
        ISlide slide1 = new Slide();
        slide1.setId("slide1");
        
        IVSVideo video1 = new VSVideo();
        video1.setId("video1");
        
        IVideoBlock videoBlock = videoBlockFactory.createVideoBlock(slide1, video1);
        Assert.assertEquals("slide1", videoBlock.getSlide().getId());
        Assert.assertEquals("video1", videoBlock.getVideo().getId());

    }
}
