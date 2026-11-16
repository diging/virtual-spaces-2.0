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
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class TextBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";

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

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        slide.setId(SLIDE_ID);
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(textBlockRepo.save(any(TextBlock.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void test_createTextBlock_success() {
        TextBlock created = new TextBlock();
        when(textBlockFactory.createTextBlock(slide, "Hello")).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(2);

        ITextBlock result = managerToTest.createTextBlock(SLIDE_ID, "Hello");

        assertSame(created, result);
        assertEquals(Integer.valueOf(3), result.getContentOrder());
        verify(textBlockRepo).save(created);
    }

    @Test
    public void test_createContentBlock_createsEmptyTextBlock() {
        TextBlock created = new TextBlock();
        when(textBlockFactory.createTextBlock(slide, "")).thenReturn(created);

        ITextBlock result = managerToTest.createContentBlock(SLIDE_ID);

        assertSame(created, result);
        assertEquals(Integer.valueOf(0), result.getContentOrder());
    }

    @Test
    public void test_updateTextBlock_saves() {
        TextBlock block = new TextBlock();

        managerToTest.updateTextBlock(block);

        verify(textBlockRepo).save(block);
    }

    @Test
    public void test_updateContentBlock_saves() {
        TextBlock block = new TextBlock();

        managerToTest.updateContentBlock(block);

        verify(textBlockRepo).save(block);
    }

    @Test
    public void test_saveContentBlock_saves() {
        TextBlock block = new TextBlock();

        managerToTest.saveContentBlock(block);

        verify(textBlockRepo).save(block);
    }
}
