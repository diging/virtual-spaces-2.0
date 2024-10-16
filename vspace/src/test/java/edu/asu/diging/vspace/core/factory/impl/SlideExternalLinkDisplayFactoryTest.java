package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;

@RunWith(MockitoJUnitRunner.class)
public class SlideExternalLinkDisplayFactoryTest {
    
    @Mock
    private IExternalLinkSlide mockExternalLink;

    @InjectMocks
    private SlideExternalLinkDisplayFactory externalLinkDisplayFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createExternalLinkDisplay_success() {
        ISlideExternalLinkDisplay externalLinkDisplay = externalLinkDisplayFactory.createExternalLinkDisplay(mockExternalLink);
        assertNotNull(externalLinkDisplay);
        assertEquals(mockExternalLink, externalLinkDisplay.getExternalLink());
    }

}
