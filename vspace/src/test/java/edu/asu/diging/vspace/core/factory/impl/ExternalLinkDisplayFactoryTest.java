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

import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;

@RunWith(MockitoJUnitRunner.class)
public class ExternalLinkDisplayFactoryTest {

    @Mock
    private IExternalLink mockExternalLink;

    @InjectMocks
    private ExternalLinkDisplayFactory externalLinkDisplayFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateExternalLinkDisplay() {
        IExternalLinkDisplay externalLinkDisplay = externalLinkDisplayFactory.createExternalLinkDisplay(mockExternalLink);
        assertNotNull(externalLinkDisplay);
        assertEquals(mockExternalLink, externalLinkDisplay.getExternalLink());
    }

}

