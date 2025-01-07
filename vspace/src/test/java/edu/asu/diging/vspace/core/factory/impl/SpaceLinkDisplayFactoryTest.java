package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;

@RunWith(MockitoJUnitRunner.class)
public class SpaceLinkDisplayFactoryTest {
    @InjectMocks
    private SpaceLinkDisplayFactory spaceLinkDisplayFactory;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createcreateSpaceLinkDisplay_success() {
        ISpaceLink link = mock(ISpaceLink.class);
        ISpaceLinkDisplay display = spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
        assertEquals(link, display.getLink());
    }

}
