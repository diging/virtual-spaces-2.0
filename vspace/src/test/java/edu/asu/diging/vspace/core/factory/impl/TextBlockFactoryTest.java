package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;

@RunWith(MockitoJUnitRunner.class)
public class TextBlockFactoryTest {
    
    @InjectMocks
    private TextBlockFactory textBlockFactoryTest = new TextBlockFactory();
    
    @Mock
    private ISlide slide;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpaceLink_success() {
   
        String text = "Test SlideTextBlock";
        ITextBlock textBlock  = textBlockFactoryTest.createTextBlock(slide, text);
        
        assertEquals(text, textBlock.getText());
        assertEquals(slide, textBlock.getSlide());
    }
}
