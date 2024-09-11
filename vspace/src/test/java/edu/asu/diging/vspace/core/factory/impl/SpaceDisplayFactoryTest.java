package edu.asu.diging.vspace.core.factory.impl;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;


@RunWith(MockitoJUnitRunner.class)
public class SpaceDisplayFactoryTest {

    @InjectMocks
    private SpaceDisplayFactory spaceDisplayFactory;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpaceDisplay_success() {
        
        ISpaceDisplay display = spaceDisplayFactory.createSpaceDisplay();
        assertNotNull(display);
        assertTrue(display instanceof SpaceDisplay);
       
    }
}
