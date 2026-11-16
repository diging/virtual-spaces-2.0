package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.factory.IChoiceBlockFactory;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.ChoiceBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class ChoiceBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private ChoiceContentBlockRepository choiceBlockRepo;

    @Mock
    private IChoiceBlockFactory choiceBlockFactory;

    @InjectMocks
    private ChoiceBlockManager managerToTest;

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(choiceBlockRepo.save(any(ChoiceBlock.class))).thenAnswer(i -> i.getArgument(0));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_createChoiceBlock_showsAllIgnoresSelectedChoices() {
        ChoiceBlock created = new ChoiceBlock();
        when(choiceBlockFactory.createChoiceBlock(eq(slide), any(List.class), eq(true))).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(3);

        IChoiceBlock result = managerToTest.createChoiceBlock(SLIDE_ID, List.of("CHO000000001"), true);

        ArgumentCaptor<List<IChoice>> choices = ArgumentCaptor.forClass(List.class);
        verify(choiceBlockFactory).createChoiceBlock(eq(slide), choices.capture(), eq(true));
        assertTrue(choices.getValue().isEmpty());
        verify(slideManager, never()).getChoice(anyString());
        assertSame(created, result);
        assertEquals(Integer.valueOf(4), result.getContentOrder());
        verify(choiceBlockRepo).save(created);
    }

    @Test
    public void test_createChoiceBlock_withSelectedChoices() {
        Choice first = new Choice();
        Choice second = new Choice();
        when(slideManager.getChoice("CHO000000001")).thenReturn(first);
        when(slideManager.getChoice("CHO000000002")).thenReturn(second);
        List<IChoice> expected = new ArrayList<>(List.of(first, second));
        ChoiceBlock created = new ChoiceBlock();
        when(choiceBlockFactory.createChoiceBlock(slide, expected, false)).thenReturn(created);

        IChoiceBlock result = managerToTest.createChoiceBlock(SLIDE_ID,
                List.of("CHO000000001", "CHO000000002"), false);

        assertSame(created, result);
        assertEquals(Integer.valueOf(0), result.getContentOrder());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_createContentBlock_showsAllChoices() {
        ChoiceBlock created = new ChoiceBlock();
        when(choiceBlockFactory.createChoiceBlock(eq(slide), any(List.class), eq(true))).thenReturn(created);

        assertSame(created, managerToTest.createContentBlock(SLIDE_ID));
    }

    @Test
    public void test_updateContentBlock_saves() {
        ChoiceBlock block = new ChoiceBlock();

        managerToTest.updateContentBlock(block);

        verify(choiceBlockRepo).save(block);
    }
}
