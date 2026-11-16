package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.SpaceContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ISpaceBlockFactory;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class SpaceBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private SpaceContentBlockRepository spaceBlockRepo;

    @Mock
    private ISpaceBlockFactory spaceBlockFactory;

    @InjectMocks
    private SpaceBlockManager managerToTest;

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(spaceBlockRepo.save(any(SpaceBlock.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void test_createSpaceBlock_success() {
        Space space = new Space();
        SpaceBlock created = new SpaceBlock();
        when(spaceBlockFactory.createSpaceBlock(slide, "Go to space", space)).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(0);

        ISpaceBlock result = managerToTest.createSpaceBlock(SLIDE_ID, "Go to space", space);

        assertSame(created, result);
        assertEquals(Integer.valueOf(1), result.getContentOrder());
        verify(spaceBlockRepo).save(created);
    }

    @Test
    public void test_createContentBlock_usesDefaults() {
        SpaceBlock created = new SpaceBlock();
        when(spaceBlockFactory.createSpaceBlock(slide, "Default Space", null)).thenReturn(created);

        assertSame(created, managerToTest.createContentBlock(SLIDE_ID));
    }

    @Test
    public void test_saveSpaceBlock_saves() {
        SpaceBlock block = new SpaceBlock();

        managerToTest.saveSpaceBlock(block);

        verify(spaceBlockRepo).save(block);
    }

    @Test
    public void test_updateContentBlock_saves() {
        SpaceBlock block = new SpaceBlock();

        managerToTest.updateContentBlock(block);

        verify(spaceBlockRepo).save(block);
    }
}
