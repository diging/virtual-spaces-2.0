package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.model.impl.VideoBlock;
import edu.asu.diging.vspace.core.services.IBiblioBlockManager;
import edu.asu.diging.vspace.core.services.IChoiceBlockManager;
import edu.asu.diging.vspace.core.services.IImageBlockManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceBlockManager;
import edu.asu.diging.vspace.core.services.ITextBlockManager;
import edu.asu.diging.vspace.core.services.IVideoBlockManager;

/**
 * {@link ContentBlockManager} delegates each block type to its own manager.
 * These tests check that every call reaches the right manager, plus the
 * slide-wide operations ContentBlockManager still implements itself. The
 * per-type behaviour is tested in the individual manager tests.
 */
public class ContentBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";
    private static final String BLOCK_ID = "CON000000001";

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private ITextBlockManager textBlockManager;

    @Mock
    private IImageBlockManager imageBlockManager;

    @Mock
    private IVideoBlockManager videoBlockManager;

    @Mock
    private ISpaceBlockManager spaceBlockManager;

    @Mock
    private IChoiceBlockManager choiceBlockManager;

    @Mock
    private IBiblioBlockManager biblioBlockManager;

    @InjectMocks
    private ContentBlockManager managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- create ----------

    @Test
    public void test_createTextBlock_delegates() {
        TextBlock block = new TextBlock();
        when(textBlockManager.createTextBlock(SLIDE_ID, "text")).thenReturn(block);

        assertSame(block, managerToTest.createTextBlock(SLIDE_ID, "text"));
    }

    @Test
    public void test_createSpaceBlock_delegates() {
        Space space = new Space();
        SpaceBlock block = new SpaceBlock();
        when(spaceBlockManager.createSpaceBlock(SLIDE_ID, "title", space)).thenReturn(block);

        assertSame(block, managerToTest.createSpaceBlock(SLIDE_ID, "title", space));
    }

    @Test
    public void test_createImageBlock_withFile_delegates() throws Exception {
        byte[] image = new byte[] { 1 };
        CreationReturnValue value = new CreationReturnValue();
        when(imageBlockManager.createImageBlock(SLIDE_ID, image, "a.png")).thenReturn(value);

        assertSame(value, managerToTest.createImageBlock(SLIDE_ID, image, "a.png"));
    }

    @Test
    public void test_createImageBlock_withExistingImage_delegates() {
        VSImage image = new VSImage();
        CreationReturnValue value = new CreationReturnValue();
        when(imageBlockManager.createImageBlock(SLIDE_ID, image)).thenReturn(value);

        assertSame(value, managerToTest.createImageBlock(SLIDE_ID, image));
    }

    @Test
    public void test_createVideoBlock_delegates() throws Exception {
        CreationReturnValue value = new CreationReturnValue();
        when(videoBlockManager.createVideoBlock(SLIDE_ID, null, null, null, "url", "title")).thenReturn(value);

        assertSame(value, managerToTest.createVideoBlock(SLIDE_ID, null, null, null, "url", "title"));
    }

    @Test
    public void test_createChoiceBlock_delegates() {
        List<String> choices = List.of("CHO000000001");
        ChoiceBlock block = new ChoiceBlock();
        when(choiceBlockManager.createChoiceBlock(SLIDE_ID, choices, false)).thenReturn(block);

        assertSame(block, managerToTest.createChoiceBlock(SLIDE_ID, choices, false));
    }

    @Test
    public void test_createBiblioBlock_delegates() {
        BiblioBlock block = new BiblioBlock();
        when(biblioBlockManager.createBiblioBlock(SLIDE_ID, "title", "desc")).thenReturn(block);

        assertSame(block, managerToTest.createBiblioBlock(SLIDE_ID, "title", "desc"));
    }

    // ---------- delete ----------

    @Test
    public void test_deleteTextBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteTextBlockById(BLOCK_ID, SLIDE_ID);
        verify(textBlockManager).deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteSpaceBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteSpaceBlockById(BLOCK_ID, SLIDE_ID);
        verify(spaceBlockManager).deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteImageBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteImageBlockById(BLOCK_ID, SLIDE_ID);
        verify(imageBlockManager).deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteVideoBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteVideoBlockById(BLOCK_ID, SLIDE_ID);
        verify(videoBlockManager).deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteChoiceBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteChoiceBlockById(BLOCK_ID, SLIDE_ID);
        verify(choiceBlockManager).deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteBiblioBlockById_delegates() throws BlockDoesNotExistException {
        managerToTest.deleteBiblioBlockById(BLOCK_ID, SLIDE_ID);
        verify(biblioBlockManager).deleteBiblioBlockById(BLOCK_ID, SLIDE_ID);
    }

    // ---------- get ----------

    @Test
    public void test_getTextBlock_delegates() {
        TextBlock block = new TextBlock();
        when(textBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getTextBlock(BLOCK_ID));
    }

    @Test
    public void test_getSpaceBlock_delegates() {
        SpaceBlock block = new SpaceBlock();
        when(spaceBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getSpaceBlock(BLOCK_ID));
    }

    @Test
    public void test_getImageBlock_delegates() {
        ImageBlock block = new ImageBlock();
        when(imageBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getImageBlock(BLOCK_ID));
    }

    @Test
    public void test_getVideoBlock_delegates() {
        VideoBlock block = new VideoBlock();
        when(videoBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getVideoBlock(BLOCK_ID));
    }

    @Test
    public void test_getChoiceBlock_delegates() {
        ChoiceBlock block = new ChoiceBlock();
        when(choiceBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getChoiceBlock(BLOCK_ID));
    }

    @Test
    public void test_getBiblioBlock_delegates() {
        BiblioBlock block = new BiblioBlock();
        when(biblioBlockManager.getContentBlock(BLOCK_ID)).thenReturn(block);
        assertSame(block, managerToTest.getBiblioBlock(BLOCK_ID));
    }

    // ---------- update / save ----------

    @Test
    public void test_updateTextBlock_delegates() {
        TextBlock block = new TextBlock();
        managerToTest.updateTextBlock(block);
        verify(textBlockManager).updateTextBlock(block);
    }

    @Test
    public void test_saveSpaceBlock_delegates() {
        SpaceBlock block = new SpaceBlock();
        managerToTest.saveSpaceBlock(block);
        verify(spaceBlockManager).saveSpaceBlock(block);
    }

    @Test
    public void test_updateImageBlock_withFile_delegates() throws Exception {
        ImageBlock block = new ImageBlock();
        byte[] image = new byte[] { 1 };
        managerToTest.updateImageBlock(block, image, "a.png");
        verify(imageBlockManager).updateImageBlock(block, image, "a.png");
    }

    @Test
    public void test_updateImageBlock_withExistingImage_delegates() {
        ImageBlock block = new ImageBlock();
        VSImage image = new VSImage();
        managerToTest.updateImageBlock(block, image);
        verify(imageBlockManager).updateImageBlock(block, image);
    }

    @Test
    public void test_updateVideoBlock_delegates() throws Exception {
        VideoBlock block = new VideoBlock();
        managerToTest.updateVideoBlock(block, null, null, "url", null, "title");
        verify(videoBlockManager).updateVideoBlock(block, null, null, "url", null, "title");
    }

    @Test
    public void test_saveVideoBlock_delegates() {
        VideoBlock block = new VideoBlock();
        managerToTest.saveVideoBlock(block);
        verify(videoBlockManager).saveVideoBlock(block);
    }

    @Test
    public void test_updateBiblioBlock_delegates() {
        BiblioBlock block = new BiblioBlock();
        managerToTest.updateBiblioBlock(block);
        verify(biblioBlockManager).updateBiblioBlock(block);
    }

    // ---------- slide-wide operations ----------

    @Test
    public void test_getAllContentBlocks_success() {
        ISlide slide = new Slide();
        ContentBlock block = new ContentBlock();
        block.setId(BLOCK_ID);
        List<IContentBlock> contents = new ArrayList<>(List.of(block));
        slide.setContents(contents);
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);

        assertSame(contents, managerToTest.getAllContentBlocks(SLIDE_ID));
    }

    @Test
    public void test_findMaxContentOrder_success() {
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(4);
        assertEquals(Integer.valueOf(4), managerToTest.findMaxContentOrder(SLIDE_ID));
    }

    @Test
    public void test_findMaxContentOrder_emptySlide() {
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        assertEquals(null, managerToTest.findMaxContentOrder(SLIDE_ID));
    }

    @Test
    public void test_updateContentOrder_success() throws BlockDoesNotExistException {
        ContentBlock stored1 = storedBlock("CON000000001", 0);
        ContentBlock stored2 = storedBlock("CON000000002", 1);
        when(contentBlockRepository.findById("CON000000001")).thenReturn(Optional.of(stored1));
        when(contentBlockRepository.findById("CON000000002")).thenReturn(Optional.of(stored2));

        List<ContentBlock> newOrder = List.of(storedBlock("CON000000001", 1), storedBlock("CON000000002", 0));
        managerToTest.updateContentOrder(newOrder);

        assertEquals(Integer.valueOf(1), stored1.getContentOrder());
        assertEquals(Integer.valueOf(0), stored2.getContentOrder());
        verify(contentBlockRepository).saveAll(List.of(stored1, stored2));
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_updateContentOrder_forNonExistentId() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById("notARealId")).thenReturn(Optional.empty());

        managerToTest.updateContentOrder(List.of(storedBlock("notARealId", 1)));
    }

    @Test
    public void test_updateContentOrder_nullListDoesNothing() throws BlockDoesNotExistException {
        managerToTest.updateContentOrder(null);
        verify(contentBlockRepository, never()).saveAll(any());
    }

    private ContentBlock storedBlock(String id, int order) {
        ContentBlock block = new ContentBlock();
        block.setId(id);
        block.setContentOrder(order);
        return block;
    }
}
