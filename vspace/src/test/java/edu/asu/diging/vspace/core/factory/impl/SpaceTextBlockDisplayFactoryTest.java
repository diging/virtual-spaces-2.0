package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

@RunWith(MockitoJUnitRunner.class)
public class SpaceTextBlockDisplayFactoryTest {
    
    @InjectMocks
    private SpaceTextBlockDisplayFactory spaceTextBlockDisplayFactory;
    
    @Mock
    private ISpaceTextBlock textBlock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpaceTextBlockDisplay_success() {
        
        float positionX = 100.0f;
        float positionY = 200.0f;
        float height = 50.0f;
        float width = 80.0f;
        String textColor = "#000000";
        String borderColor = "#ffffff";
        
        ISpaceTextBlockDisplay display= spaceTextBlockDisplayFactory.createSpaceTextBlockDisplay(textBlock, positionX, positionY, height, width, textColor, borderColor);
        
        assertEquals(textBlock, display.getSpaceTextBlock());
        assertEquals(positionX, display.getPositionX(), 0.001); 
        assertEquals(positionY, display.getPositionY(), 0.001);
        assertEquals(height, display.getHeight(), 0.001);
        assertEquals(width, display.getWidth(), 0.001);
        
    }

}
