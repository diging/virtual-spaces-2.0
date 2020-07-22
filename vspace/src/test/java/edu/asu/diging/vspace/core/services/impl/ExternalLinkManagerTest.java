package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;

public class ExternalLinkManagerTest {
    @Mock
    private ExternalLinkRepository externalLinkRepo;

    @Mock
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;

    @InjectMocks
    private ExternalLinkManager managerToTest = new ExternalLinkManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getDisplayLink_idExists() {
        ExternalLinkDisplay newExternalLinkDisplay = new ExternalLinkDisplay();
        newExternalLinkDisplay.setId("EXLD001");
        Optional<ExternalLinkDisplay> mockExternalLinkDisplay = Optional.of(newExternalLinkDisplay);
        Mockito.when(externalLinkDisplayRepo.findById(newExternalLinkDisplay.getId())).thenReturn(mockExternalLinkDisplay);

        ExternalLinkDisplay externalLinkDisplayActual = managerToTest.getDisplayLink(newExternalLinkDisplay.getId());
        Assert.assertEquals(mockExternalLinkDisplay.get().getId(), externalLinkDisplayActual.getId());
    }

    @Test
    public void test_getDisplayLink_idNotExists() throws Exception {
        Mockito.when(externalLinkDisplayRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getDisplayLink(Mockito.anyString()));
    }

    @Test
    public void test_getLink_idExists() {
        ExternalLink newExternalLink = new ExternalLink();
        newExternalLink.setId("MOL001");
        Optional<ExternalLink> mockExternalLink = Optional.of(newExternalLink);
        Mockito.when(externalLinkRepo.findById(newExternalLink.getId())).thenReturn(mockExternalLink);

        IExternalLink externalLinkActual = managerToTest.getLink(newExternalLink.getId());
        Assert.assertEquals(mockExternalLink.get().getId(), externalLinkActual.getId());
    }

    @Test
    public void test_getLink_idNotExists() throws Exception {
        Mockito.when(externalLinkRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getLink(Mockito.anyString()));
    }
}
