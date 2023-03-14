package edu.asu.diging.vspace.core.factory.impl;

import org.junit.Assert;
import org.junit.Test;

import edu.asu.diging.vspace.core.factory.impl.VideoFactory;
import edu.asu.diging.vspace.core.model.IVSVideo;

public class VideoFactoryTest {
    
    private VideoFactory videoFactoryToTest = new VideoFactory();
    
    @Test
    public void test_createVideoFile_success() {
        IVSVideo video =  videoFactoryToTest.createVideo("video1", 1024L, "video/mp4");
        Assert.assertEquals("video1", video.getFilename());
        Assert.assertEquals(Long.valueOf(1024L), video.getFileSize());
        Assert.assertEquals("video/mp4", video.getFileType());
        Assert.assertEquals(null, video.getUrl());
    }
    
    @Test
    public void test_createVideoUrl_success() {
        IVSVideo video = videoFactoryToTest.createVideo("www.testVideo.com/sampleVideo");
        Assert.assertEquals("www.testVideo.com/sampleVideo", video.getUrl());
    }

}
