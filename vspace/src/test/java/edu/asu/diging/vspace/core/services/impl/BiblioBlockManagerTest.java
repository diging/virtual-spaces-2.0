package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class BiblioBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";
    private static final String BLOCK_ID = "CON000000005";

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private BiblioBlockRepository biblioBlockRepo;

    @InjectMocks
    private BiblioBlockManager managerToTest;

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(biblioBlockRepo.save(any(BiblioBlock.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void test_createBiblioBlock_success() {
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(1);

        managerToTest.createBiblioBlock(SLIDE_ID, "Further reading", "Sources for this slide");

        ArgumentCaptor<BiblioBlock> captor = ArgumentCaptor.forClass(BiblioBlock.class);
        verify(biblioBlockRepo).save(captor.capture());
        BiblioBlock saved = captor.getValue();
        assertEquals("Further reading", saved.getBiblioTitle());
        assertEquals("Sources for this slide", saved.getDescription());
        assertSame(slide, saved.getSlide());
        assertEquals(Integer.valueOf(2), saved.getContentOrder());
    }

    @Test
    public void test_createContentBlock_usesDefaults() {
        BiblioBlock result = (BiblioBlock) managerToTest.createContentBlock(SLIDE_ID);

        assertEquals("Default Bibliography", result.getBiblioTitle());
        assertEquals("Default Description", result.getDescription());
        assertEquals(Integer.valueOf(0), result.getContentOrder());
    }

    @Test
    public void test_deleteBiblioBlockById_success() throws BlockDoesNotExistException {
        ContentBlock block = new ContentBlock();
        block.setContentOrder(0);
        when(contentBlockRepository.findById(BLOCK_ID)).thenReturn(Optional.of(block));

        managerToTest.deleteBiblioBlockById(BLOCK_ID, SLIDE_ID);

        verify(biblioBlockRepo).deleteById(BLOCK_ID);
        verify(contentBlockRepository).findBySlide_IdAndContentOrderGreaterThan(SLIDE_ID, 0);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteBiblioBlockById_forNonExistentId() throws BlockDoesNotExistException {
        when(contentBlockRepository.findById("notARealId")).thenReturn(Optional.empty());

        managerToTest.deleteBiblioBlockById("notARealId", SLIDE_ID);
    }

    @Test
    public void test_deleteBiblioBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        managerToTest.deleteBiblioBlockById(null, SLIDE_ID);

        verify(biblioBlockRepo, never()).deleteById(any());
    }

    @Test
    public void test_getContentBlock_success() {
        BiblioBlock block = new BiblioBlock();
        when(biblioBlockRepo.findById(BLOCK_ID)).thenReturn(Optional.of(block));

        assertSame(block, managerToTest.getContentBlock(BLOCK_ID));
    }

    @Test
    public void test_updateBiblioBlock_saves() {
        BiblioBlock block = new BiblioBlock();

        managerToTest.updateBiblioBlock(block);

        verify(biblioBlockRepo).save(block);
    }
}
