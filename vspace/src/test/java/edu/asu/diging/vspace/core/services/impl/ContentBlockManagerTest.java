package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoContentBlockRepository;
import edu.asu.diging.vspace.core.data.VideoRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.ReferenceListDeletionForBiblioException;
import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoBlockFactory;
import edu.asu.diging.vspace.core.factory.IVideoFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VSVideo;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;

public class ContentBlockManagerTest {

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;


    
    @Mock
    private BiblioBlockRepository biblioBlockRepo;
    
    @Mock
    private ReferenceRepository refRepo;
    
    @Mock
    private SlideManager slideManager;
    
    @Mock
    private ReferenceManager refManager;
    

    @Mock
    private ChoiceContentBlockRepository choiceBlockRepo;
    
    @Mock
    private VideoContentBlockRepository videoBlockRepo;

    @Mock
    private IVideoFactory videoFactory;
    
    @Mock
    private IImageBlockFactory imageBlockFactory;
    
    @Mock
    private ImageRepository imageRepo;
    
    @Mock
    private IImageFactory imageFactory;
    
    @Mock
    private ITextBlockFactory textBlockFactory;

    @Mock
    private IVideoBlockFactory videoBlockFactory;

    

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
    public void test_getAllContentBlocks_success() {
        ISlide slide = new Slide();
        slide.setId("Slide1");
        slide.setContents(new ArrayList<IContentBlock>());
        when(slideManager.getSlide("Slide1")).thenReturn(slide);
        assertEquals(managerToTest.getAllContentBlocks("Slide1"), slide.getContents());
    }
    
    @Test
    public void test_createTextBlock_success() {
        ISlide slide = new Slide();
        slide.setId("Slide1");
        ITextBlock textBlock = new TextBlock();
        textBlock.setText("Test");
        textBlock.setSlide(slide);
        when(textBlockRepo.save((TextBlock)textBlock)).thenReturn((TextBlock) textBlock);
        when(slideManager.getSlide("Slide1")).thenReturn(slide);
        when(textBlockFactory.createTextBlock(slide, "Test")).thenReturn(textBlock);
        managerToTest.createTextBlock("Slide1", "Test", 1);
        assertEquals(textBlock.getContentOrder(), (Integer)1);
        assertEquals(managerToTest.createTextBlock("Slide1", "Test", 1), textBlock);
    }
    
    @Test
    public void test_createImageBlock_success() throws ImageCouldNotBeStoredException, FileStorageException {
        ISlide slide = new Slide();
        slide.setId("Slide1");
        IVSImage slideContentImage = new VSImage();
        slideContentImage.setFilename("img.jpg");
        slideContentImage.setFileType("application/octet-stream");
        slideContentImage.setId("1");
        IImageBlock imgBlock = new ImageBlock();
        imgBlock.setSlide(slide);
        imgBlock.setImage(slideContentImage);
        when(slideManager.getSlide("Slide1")).thenReturn(slide);
        when(imageBlockFactory.createImageBlock(slide, slideContentImage)).thenReturn(imgBlock);
        when(storage.storeFile(new byte[20], "img.jpg", "1")).thenReturn("path");
        when(imageFactory.createImage("img.jpg", "application/octet-stream")).thenReturn(slideContentImage);
        when(imageRepo.save((VSImage) slideContentImage)).thenReturn((VSImage) slideContentImage);
        when(imageBlockRepo.save((ImageBlock) imgBlock)).thenReturn((ImageBlock) imgBlock);
        CreationReturnValue returnValue = managerToTest.createImageBlock("Slide1", new byte[20], "img.jpg", 1);
        assertEquals(imgBlock.getContentOrder(), (Integer)1);
        assertEquals((ImageBlock)returnValue.getElement(), imgBlock);
    }
    
    @Test
    public void test_createImageBlock_withId_success() {
        ISlide slide = new Slide();
        slide.setId("Slide1");
        IVSImage slideContentImage = new VSImage();
        slideContentImage.setFilename("img.jpg");
        slideContentImage.setFileType("application/octet-stream");
        slideContentImage.setId("1");
        IImageBlock imgBlock = new ImageBlock();
        imgBlock.setSlide(slide);
        imgBlock.setImage(slideContentImage);
        when(slideManager.getSlide("Slide1")).thenReturn(slide);
        when(imageBlockFactory.createImageBlock(slide, slideContentImage)).thenReturn(imgBlock);
        when(imageBlockRepo.save((ImageBlock) imgBlock)).thenReturn((ImageBlock) imgBlock);
        CreationReturnValue returnValue = managerToTest.createImageBlock("Slide1", slideContentImage, 1);
        assertEquals(imgBlock.getContentOrder(), (Integer)1);
        assertEquals((ImageBlock)returnValue.getElement(), imgBlock);
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
    public void test_deleteBiblioBlockById_success() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "CON000000002";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("CON000000002")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1",Integer.valueOf(1))).thenReturn(contentBlockList);
        managerToTest.deleteBiblioBlockById(biblioBlockId,"slideId_1");
        Mockito.verify(biblioBlockRepo).deleteById(biblioBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder()).collect(Collectors.toList());
        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));
        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }
    
    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteBiblioBlockById_forNonExistentId() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(biblioBlockRepo).deleteById(biblioBlockId);
        managerToTest.deleteBiblioBlockById(biblioBlockId, "slideId_1");
    }

    @Test
    public void test_deleteBiblioBlockById_whenIdIsNull() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = null;
        managerToTest.deleteBiblioBlockById(null, "slideId_1");
        Mockito.verify(biblioBlockRepo, Mockito.never()).deleteById(biblioBlockId);
    }
    
    @Test
    public void test_createBiblioBlock_success() {

        Integer contentOrder = 1;
        String title = "title1";
        String description = "description1";

        Slide slide = new Slide();
        slide.setId("slide1");
        
        IBiblioBlock biblioBlock = new BiblioBlock();
        biblioBlock.setBiblioTitle("TestTitle");
        biblioBlock.setDescription("Test Description");
        
        IBiblioBlock biblioBlockWithId = new BiblioBlock();
        biblioBlockWithId.setId("CON00000001");
        biblioBlockWithId.setBiblioTitle("TestTitle");
        biblioBlockWithId.setDescription("Test Description");
        
        biblioBlock.setContentOrder(contentOrder);
        biblioBlock.setSlide(slide);
        
        when(slideManager.getSlide(slide.getId())).thenReturn(slide);
        when(biblioBlockRepo.save((BiblioBlock)biblioBlock)).thenReturn((BiblioBlock) biblioBlockWithId);
        IBiblioBlock resBiblio = managerToTest.createBiblioBlock(slide.getId(), title, description, contentOrder);
        assertEquals(resBiblio.getId(), biblioBlockWithId.getId());
        assertEquals(resBiblio.getBiblioTitle(), biblioBlockWithId.getBiblioTitle());
        assertEquals(resBiblio.getDescription(), biblioBlockWithId.getDescription());
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
    public void test_deleteChoiceBlockById_success() throws BlockDoesNotExistException {
        ContentBlock contentBlock1 = new ContentBlock();
        contentBlock1.setId("notARealId");
        contentBlock1.setContentOrder(Integer.valueOf(1));

        List<ContentBlock> contentBlocks = new ArrayList<>();
        contentBlocks.add(contentBlock1);
        
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("Block1")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("Slide1", 1)).thenReturn(contentBlocks);
        when(contentBlockRepository.saveAll(contentBlockList)).thenReturn(null);
        managerToTest.deleteChoiceBlockById("Block1", "Slide1");
        Mockito.verify(choiceBlockRepo).deleteById("Block1");
    }
    
    @Test
    public void test_updateTextBlock_success() {
        TextBlock textBlock = new TextBlock();
        managerToTest.updateTextBlock(textBlock);
        Mockito.verify(textBlockRepo).save(textBlock);
    }
    
    @Test
    public void test_saveVideoBlock_success() {
        IVideoBlock vidBlock = new VideoBlock();
        IVSVideo slideContentVideo = new VSVideo();
        vidBlock.setVideo(slideContentVideo);
        managerToTest.saveVideoBlock(vidBlock);
        Mockito.verify(videoRepo).save((VSVideo) slideContentVideo);
    }
    
    @Test
    public void test_findMaxContentOrder_success() {
        when(contentBlockRepository.findMaxContentOrder("Slide1")).thenReturn(1);
        assertEquals(managerToTest.findMaxContentOrder("Slide1"), (Integer)1);
    }
    
    @Test
    public void test_findMaxContentOrder_failure() {
        when(contentBlockRepository.findMaxContentOrder(null)).thenReturn(0);
        assertEquals(managerToTest.findMaxContentOrder(null), (Integer)0);
    }
    
    @Test
    public void test_getTextBlock_success() {
        TextBlock textBlock = new TextBlock(); 
        Optional<TextBlock> textBlockOptional = Optional.of(textBlock);
        when(textBlockRepo.findById("Text1")).thenReturn(textBlockOptional);
        assertEquals(managerToTest.getTextBlock("Text1"), textBlock);
    }
    
    @Test
    public void test_getTextBlock_failure() {
        Optional<TextBlock> textBlockOptional = Optional.empty();
        when(textBlockRepo.findById("Text1")).thenReturn(textBlockOptional);
        assertEquals(managerToTest.getTextBlock("Text1"), null);
    }

    @Test
    public void test_getVideoBlock_success() {
        String videoBlockId = "videoBlockId_3";
        Optional<VideoBlock> contentBlockOptional = Optional.of(videoBlock);
        when(videoBlockRepo.findById(videoBlockId)).thenReturn(contentBlockOptional);
        IVideoBlock videoBlock = managerToTest.getVideoBlock(videoBlockId);
        assertEquals(videoBlockId, videoBlock.getId());
    }
    
    @Test
    public void test_getVideoBlock_failure() {
        String videoBlockId = "videoBlockId_3";
        Optional<VideoBlock> contentBlockOptional = Optional.empty();
        when(videoBlockRepo.findById(videoBlockId)).thenReturn(contentBlockOptional);
        assertEquals(managerToTest.getVideoBlock(videoBlockId), null);
    }
    
    @Test
    public void test_getChoiceBlock_success() {
        ChoiceBlock choiceBlockId = new ChoiceBlock();
        Optional<ChoiceBlock> choiceBlockOptional = Optional.of(choiceBlockId);
        when(choiceBlockRepo.findById("Choice1")).thenReturn(choiceBlockOptional);
        assertEquals(managerToTest.getChoiceBlock("Choice1"), choiceBlockId);
    }
    
    @Test
    public void test_getChoiceBlock_failure() {
        Optional<ChoiceBlock> choiceBlockOptional = Optional.empty();
        when(choiceBlockRepo.findById("Choice1")).thenReturn(choiceBlockOptional);
        assertEquals(managerToTest.getChoiceBlock("Choice1"), null);
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
    
    @Test
    public void test_createVideoBlock_withVideo()
            throws BlockDoesNotExistException, FileStorageException, VideoCouldNotBeStoredException, IOException {
        ISlide slide = new Slide();
        slide.setId("slideId_1");
        when(slideManager.getSlide("slideId_1")).thenReturn(slide);
        IVSVideo slideContentVideo = new VSVideo();
        slideContentVideo.setId("videoId_1");
        when(videoFactory.createVideo("newFile.mp4", 200L, "application/octet-stream")).thenReturn(slideContentVideo);
        IVideoBlock vidBlock = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(slide, slideContentVideo)).thenReturn(vidBlock);
        when(videoRepo.save((VSVideo) slideContentVideo)).thenReturn((VSVideo) slideContentVideo);
        VideoBlock videoBlock = new VideoBlock();
        videoBlock.setId("videoBlock_1");
        when(videoBlockRepo.save((VideoBlock) vidBlock)).thenReturn(videoBlock);
        CreationReturnValue returnValue = managerToTest.createVideoBlock("slideId_1", new byte[20], 200L, "newFile.mp4", null, 1,"video_title");
        VideoBlock vblk = (VideoBlock) returnValue.getElement();
        assertEquals(vblk.getId(), "videoBlock_1");
    }
    
    @Test
    public void test_createVideoBlock_withVideoURLnull()
            throws BlockDoesNotExistException, FileStorageException, VideoCouldNotBeStoredException, IOException {
        ISlide slide = new Slide();
        slide.setId("slideId_1");
        when(slideManager.getSlide("slideId_1")).thenReturn(slide);
        IVSVideo slideContentVideo = null;
        IVideoBlock vidBlock = new VideoBlock();
        when(videoBlockFactory.createVideoBlock(slide, slideContentVideo)).thenReturn(vidBlock);
        when(videoRepo.save((VSVideo) slideContentVideo)).thenReturn((VSVideo) slideContentVideo);
        VideoBlock videoBlock = new VideoBlock();
        videoBlock.setId("videoBlock_1");
        when(videoBlockRepo.save((VideoBlock) vidBlock)).thenReturn(videoBlock);
        CreationReturnValue returnValue = managerToTest.createVideoBlock("slideId_1", null, 200L, null, null, 1,
                "video_title");
        VideoBlock vblk = (VideoBlock) returnValue.getElement();
        assertEquals(vblk.getId(), "videoBlock_1");
    }

}
