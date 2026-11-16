package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

/**
 * Tests the shared behaviour in {@link GenericContentBlockManager} (get, delete
 * and content order handling) through {@link TextBlockManager}, its simplest
 * concrete subclass.
 */
public class GenericContentBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";
    private static final String BLOCK_ID = "CON000000002";

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ITextBlockFactory textBlockFactory;

    @InjectMocks
    private TextBlockManager managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_getContentBlock_found() {
        TextBlock block = new TextBlock();
        when(textBlockRepo.findById(BLOCK_ID)).thenReturn(Optional.of(block));

        assertSame(block, managerToTest.getContentBlock(BLOCK_ID));
    }

    @Test
    public void test_getContentBlock_notFound() {
        when(textBlockRepo.findById(BLOCK_ID)).thenReturn(Optional.empty());

        assertNull(managerToTest.getContentBlock(BLOCK_ID));
    }

    @Test
    public void test_deleteContentBlock_nullIdDoesNothing() throws BlockDoesNotExistException {
        managerToTest.deleteContentBlock(null, SLIDE_ID);

        verifyNoInteractions(contentBlockRepository, textBlockRepo);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteContentBlock_missingBlock() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.empty());

        managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_deleteContentBlock_missingBlockDoesNotDelete() {
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.empty());

        try {
            managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);
        } catch (BlockDoesNotExistException e) {
            // expected
        }
        verify(textBlockRepo, never()).deleteById(anyString());
    }

    @Test
    public void test_deleteContentBlock_shiftsLaterBlocksUp() throws BlockDoesNotExistException {
        ContentBlock deleted = blockWithOrder(BLOCK_ID, 1);
        ContentBlock later1 = blockWithOrder("CON000000003", 2);
        ContentBlock later2 = blockWithOrder("CON000000004", 3);
        List<ContentBlock> laterBlocks = new ArrayList<>(List.of(later1, later2));

        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.of(deleted));
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan(SLIDE_ID, 1)).thenReturn(laterBlocks);

        managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);

        verify(textBlockRepo).deleteById(BLOCK_ID);
        assertEquals(Integer.valueOf(1), later1.getContentOrder());
        assertEquals(Integer.valueOf(2), later2.getContentOrder());
        verify(contentBlockRepository).saveAll(laterBlocks);
    }

    @Test
    public void test_deleteContentBlock_noLaterBlocks() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.of(blockWithOrder(BLOCK_ID, 5)));
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan(SLIDE_ID, 5)).thenReturn(null);

        managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);

        verify(textBlockRepo).deleteById(BLOCK_ID);
        verify(contentBlockRepository, never()).saveAll(any());
    }

    @Test
    public void test_deleteContentBlock_blockWithoutOrderSkipsReordering() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.of(blockWithOrder(BLOCK_ID, null)));

        managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);

        verify(textBlockRepo).deleteById(BLOCK_ID);
        verify(contentBlockRepository, never()).findBySlide_IdAndContentOrderGreaterThan(anyString(), any());
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteContentBlock_repositoryReportsMissing() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.of(blockWithOrder(BLOCK_ID, 1)));
        doThrow(new EmptyResultDataAccessException(1)).when(textBlockRepo).deleteById(BLOCK_ID);

        managerToTest.deleteContentBlock(BLOCK_ID, SLIDE_ID);
    }

    @Test
    public void test_createBlock_firstBlockOnSlideGetsOrderZero() {
        stubTextCreation();
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);

        ITextBlock block = managerToTest.createTextBlock(SLIDE_ID, "text");

        assertEquals(Integer.valueOf(0), block.getContentOrder());
    }

    @Test
    public void test_createBlock_appendsAfterLastBlock() {
        stubTextCreation();
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(4);

        ITextBlock block = managerToTest.createTextBlock(SLIDE_ID, "text");

        assertEquals(Integer.valueOf(5), block.getContentOrder());
    }

    private void stubTextCreation() {
        Slide slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        when(textBlockFactory.createTextBlock(slide, "text")).thenReturn(new TextBlock());
        when(textBlockRepo.save(any(TextBlock.class))).thenAnswer(i -> i.getArgument(0));
    }

    private ContentBlock blockWithOrder(String id, Integer order) {
        ContentBlock block = new ContentBlock();
        block.setId(id);
        block.setContentOrder(order);
        return block;
    }
}
