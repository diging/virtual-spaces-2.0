package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;

@RunWith(MockitoJUnitRunner.class)
public class SpaceTextBlockFactoryTest {
    @InjectMocks
    private SpaceTextBlockFactory spaceTextBlockFactory = new SpaceTextBlockFactory();
    
    @Mock
    private ISpace space;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpaceLink_success() {
   
        String text = "Test SpaceTextBlock";
        ISpaceTextBlock spaceTextBlock  = spaceTextBlockFactory.createSpaceTextBlock(text, space);
        
        assertEquals(text, spaceTextBlock.getText());
        assertEquals(space, spaceTextBlock.getSpace());
    }       
}
