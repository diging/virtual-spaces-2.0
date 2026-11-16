package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class VideoBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";
    private static final String VIDEO_ID = "VID000000001";
    private static final String URL = "https://www.youtube.com/embed/abc";
    private static final byte[] VIDEO_BYTES = new byte[] { 0, 0, 0, 0x18, 'f', 't', 'y', 'p', 'm', 'p', '4', '2' };

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private VideoContentBlockRepository videoBlockRepo;

    @Mock
    private VideoRepository videoRepo;

    @Mock
    private IVideoFactory videoFactory;

    @Mock
    private IVideoBlockFactory videoBlockFactory;

    @Mock
    private IStorageEngine storage;

    @InjectMocks
    private VideoBlockManager managerToTest;

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(videoBlockRepo.save(any(VideoBlock.class))).thenAnswer(i -> i.getArgument(0));
        when(videoRepo.save(any(VSVideo.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void test_createVideoBlock_withUrl() throws Exception {
        VSVideo video = new VSVideo();
        when(videoFactory.createVideo(URL)).thenReturn(video);
        VideoBlock created = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(slide, video)).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(2);

        CreationReturnValue result = managerToTest.createVideoBlock(SLIDE_ID, null, null, null, URL, "Intro");

        assertSame(created, result.getElement());
        assertEquals("Intro", video.getTitle());
        assertEquals(Integer.valueOf(3), created.getContentOrder());
        verify(storage, never()).storeFile(any(), anyString(), anyString());
        verify(videoBlockRepo).save(created);
    }

    @Test
    public void test_createVideoBlock_withUploadedFile() throws Exception {
        VSVideo video = new VSVideo();
        video.setId(VIDEO_ID);
        video.setUrl("should be cleared");
        when(videoFactory.createVideo(eq("clip.mp4"), eq(12L), anyString())).thenReturn(video);
        when(storage.storeFile(VIDEO_BYTES, "clip.mp4", VIDEO_ID)).thenReturn("videos/path");
        VideoBlock created = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(slide, video)).thenReturn(created);

        CreationReturnValue result = managerToTest.createVideoBlock(SLIDE_ID, VIDEO_BYTES, 12L, "clip.mp4", URL, "Clip");

        assertSame(created, result.getElement());
        assertEquals("Clip", video.getTitle());
        assertEquals("videos/path", video.getParentPath());
        assertNull(video.getUrl());
        assertEquals(Integer.valueOf(0), created.getContentOrder());
    }

    @Test(expected = VideoCouldNotBeStoredException.class)
    public void test_createVideoBlock_storageFailure() throws Exception {
        VSVideo video = new VSVideo();
        video.setId(VIDEO_ID);
        when(videoFactory.createVideo(eq("clip.mp4"), eq(12L), anyString())).thenReturn(video);
        when(storage.storeFile(VIDEO_BYTES, "clip.mp4", VIDEO_ID)).thenThrow(new FileStorageException("disk full"));

        managerToTest.createVideoBlock(SLIDE_ID, VIDEO_BYTES, 12L, "clip.mp4", null, "Clip");
    }

    @Test
    public void test_createVideoBlock_withoutFileOrUrl() throws Exception {
        VideoBlock created = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(eq(slide), isNull())).thenReturn(created);

        CreationReturnValue result = managerToTest.createVideoBlock(SLIDE_ID, null, null, null, "", "No video");

        assertSame(created, result.getElement());
        verify(videoRepo, never()).save(any());
    }

    @Test
    public void test_createContentBlock_createsBlockWithoutVideo() {
        VideoBlock created = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(eq(slide), isNull())).thenReturn(created);

        IVideoBlock result = managerToTest.createContentBlock(SLIDE_ID);

        assertSame(created, result);
    }

    @Test
    public void test_updateVideoBlock_withUrl() throws Exception {
        VSVideo video = new VSVideo();
        when(videoFactory.createVideo(URL)).thenReturn(video);
        VideoBlock block = new VideoBlock();

        managerToTest.updateVideoBlock(block, null, null, URL, null, "Updated");

        assertSame(video, block.getVideo());
        assertEquals("Updated", video.getTitle());
        verify(videoBlockRepo).save(block);
    }

    @Test
    public void test_saveVideoBlock_savesVideo() {
        VSVideo video = new VSVideo();
        VideoBlock block = new VideoBlock();
        block.setVideo(video);

        managerToTest.saveVideoBlock(block);

        verify(videoRepo).save(video);
    }
}
