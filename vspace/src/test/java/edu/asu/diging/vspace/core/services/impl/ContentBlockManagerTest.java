package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class ContentBlockManagerTest {

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;

    @Mock
    private VideoContentBlockRepository videoBlockRepo;

    @Mock
    private IVideoFactory videoFactory;

    @Mock
    private IVideoBlockFactory videoBlockFactory;

    @Mock
    private ISlideManager slideManager;

    @Mock
    private IStorageEngine storage;

    @Mock
    private VideoRepository videoRepo;

    @InjectMocks
    private ContentBlockManager managerToTest;

    private ContentBlock contentBlock;

    private ContentBlock contentBlock1;

    private List<ContentBlock> contentBlockList;

    private VideoBlock videoBlock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        contentBlock = new ContentBlock();
        contentBlock.setContentOrder(1);

        contentBlock1 = new ContentBlock();
        contentBlock1.setContentOrder(2);
        ContentBlock contentBlock2 = new ContentBlock();
        contentBlock2.setContentOrder(3);
        contentBlockList = new ArrayList<>();
        contentBlockList.add(contentBlock1);
        contentBlockList.add(contentBlock2);

        videoBlock = new VideoBlock();
        videoBlock.setId("videoBlockId_3");
        videoBlock.setContentOrder(4);
    }

    @Test
    public void test_deleteTextBlockById_success() throws BlockDoesNotExistException {
        String textBlockId = "textBlockId_2";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("textBlockId_2")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1", Integer.valueOf(1)))
                .thenReturn(contentBlockList);
        managerToTest.deleteTextBlockById(textBlockId, "slideId_1");
        Mockito.verify(textBlockRepo).deleteById(textBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder())
                .collect(Collectors.toList());

        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));

        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String textBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).deleteById(textBlockId);
        managerToTest.deleteTextBlockById(textBlockId, "slideId_1");
    }

    @Test
    public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String textBlockId = null;
        managerToTest.deleteTextBlockById(null, "slideId_1");
        Mockito.verify(textBlockRepo, Mockito.never()).deleteById(textBlockId);
    }

    @Test
    public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
        String imageBlockId = "imageBlockId_2";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("imageBlockId_2")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1", Integer.valueOf(1)))
                .thenReturn(contentBlockList);
        managerToTest.deleteImageBlockById(imageBlockId, "slideId_1");
        Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder())
                .collect(Collectors.toList());

        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));

        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImageBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String imageBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(imageBlockRepo).deleteById(imageBlockId);
        managerToTest.deleteImageBlockById(imageBlockId, "slideId_1");
    }

    @Test
    public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String imageBlockId = null;
        managerToTest.deleteImageBlockById(null, "slideId_1");
        Mockito.verify(imageBlockRepo, Mockito.never()).deleteById(imageBlockId);

    }

    @Test
    public void test_updateContentOrder_success() throws BlockDoesNotExistException {
        ContentBlock firstContentBlock = new ContentBlock();
        firstContentBlock.setId("contentBlockId1");
        firstContentBlock.setContentOrder(Integer.valueOf(3));

        ContentBlock secondContentBlock = new ContentBlock();
        secondContentBlock.setId("contentBlockId2");
        secondContentBlock.setContentOrder(Integer.valueOf(4));

        List<ContentBlock> contentBlocks = new ArrayList<>();
        contentBlocks.add(firstContentBlock);
        contentBlocks.add(secondContentBlock);

        when(contentBlockRepository.findById("contentBlockId1")).thenReturn(Optional.of(contentBlock));
        when(contentBlockRepository.findById("contentBlockId2")).thenReturn(Optional.of(contentBlock1));

        managerToTest.updateContentOrder(contentBlocks);
        assertEquals(Integer.valueOf(3), contentBlock.getContentOrder());
        assertEquals(Integer.valueOf(4), contentBlock1.getContentOrder());

        List<ContentBlock> temp_contentBlockList = new ArrayList<>();
        temp_contentBlockList.add(contentBlock);
        temp_contentBlockList.add(contentBlock1);

        Mockito.verify(contentBlockRepository).saveAll(temp_contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_updateContentOrder_forNonExistentId() throws BlockDoesNotExistException {
        ContentBlock contentBlock1 = new ContentBlock();
        contentBlock1.setId("notARealId");
        contentBlock1.setContentOrder(Integer.valueOf(1));

        List<ContentBlock> contentBlocks = new ArrayList<>();
        contentBlocks.add(contentBlock1);

        when(contentBlockRepository.findById("notARealId")).thenReturn(Optional.empty());
        managerToTest.updateContentOrder(contentBlocks);
    }

    @Test
    public void test_deleteVideoBlockById_success() throws BlockDoesNotExistException {
        String videoBlockId = "videoBlockId_2";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById(videoBlockId)).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1", Integer.valueOf(1)))
                .thenReturn(contentBlockList);
        managerToTest.deleteVideoBlockById(videoBlockId, "slideId_1");
        Mockito.verify(videoBlockRepo).deleteById(videoBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder())
                .collect(Collectors.toList());

        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));

        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteVideoBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String videoBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(videoBlockRepo).deleteById(videoBlockId);
        managerToTest.deleteVideoBlockById(videoBlockId, "slideId_1");
    }

    @Test
    public void test_deleteVideoBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String videoBlockId = null;
        managerToTest.deleteTextBlockById(null, "slideId_1");
        Mockito.verify(videoBlockRepo, Mockito.never()).deleteById(videoBlockId);
    }

    @Test
    public void test_getVideoBlock_success() throws BlockDoesNotExistException {
        String videoBlockId = "videoBlockId_3";
        Optional<VideoBlock> contentBlockOptional = Optional.of(videoBlock);
        when(videoBlockRepo.findById(videoBlockId)).thenReturn(contentBlockOptional);
        IVideoBlock videoBlock = managerToTest.getVideoBlock(videoBlockId);
        assertEquals(videoBlockId, videoBlock.getId());
    }
    
    @Test
    public void test_createVideoBlock_withURLsuccess()
            throws BlockDoesNotExistException, FileStorageException, VideoCouldNotBeStoredException, IOException {
        ISlide slide = new Slide();
        slide.setId("slideId_1");
        when(slideManager.getSlide("slideId_1")).thenReturn(slide);
        IVSVideo slideContentVideo = new VSVideo();
        slideContentVideo.setId("videoId_1");
        when(videoFactory.createVideo("https://www.youtube.com/watch?v=cF6katdKoVM")).thenReturn(slideContentVideo);
        IVideoBlock vidBlock = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(slide, slideContentVideo)).thenReturn(vidBlock);
        when(videoRepo.save((VSVideo) slideContentVideo)).thenReturn((VSVideo) slideContentVideo);
        VideoBlock videoBlock = new VideoBlock();
        videoBlock.setId("videoBlock_1");
        when(videoBlockRepo.save((VideoBlock) vidBlock)).thenReturn(videoBlock);
        CreationReturnValue returnValue = managerToTest.createVideoBlock("slideId_1", null, 200L, null, "https://www.youtube.com/watch?v=cF6katdKoVM", 1,
                "video_title");
        VideoBlock vblk = (VideoBlock) returnValue.getElement();
        assertEquals(vblk.getId(), "videoBlock_1");
    }

}
