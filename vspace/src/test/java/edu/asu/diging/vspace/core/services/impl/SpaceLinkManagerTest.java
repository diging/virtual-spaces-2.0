package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

public class SpaceLinkManagerTest {
    @Mock
    private SpaceLinkRepository spaceLinkRepo;

    @Mock
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    @InjectMocks
    private SpaceLinkManager managerToTest = new SpaceLinkManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getDisplayLink_idExists() {
        SpaceLinkDisplay newSpaceLinkDisplay = new SpaceLinkDisplay();
        newSpaceLinkDisplay.setId("SPLD001");
        Optional<SpaceLinkDisplay> mockSpaceLinkDisplay = Optional.of(newSpaceLinkDisplay);
        Mockito.when(spaceLinkDisplayRepo.findById(newSpaceLinkDisplay.getId())).thenReturn(mockSpaceLinkDisplay);

        SpaceLinkDisplay spaceLinkDisplayActual = managerToTest.getDisplayLink(newSpaceLinkDisplay.getId());
        Assert.assertEquals(mockSpaceLinkDisplay.get().getId(), spaceLinkDisplayActual.getId());
    }

    @Test
    public void test_getDisplayLink_idNotExists() throws Exception {
        Mockito.when(spaceLinkDisplayRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getDisplayLink(Mockito.anyString()));
    }

    @Test
    public void test_getLink_idExists() {
        SpaceLink newSpaceLink = new SpaceLink();
        newSpaceLink.setId("MOL001");
        Optional<SpaceLink> mockSpaceLink = Optional.of(newSpaceLink);
        Mockito.when(spaceLinkRepo.findById(newSpaceLink.getId())).thenReturn(mockSpaceLink);

        ISpaceLink spaceLinkActual = managerToTest.getLink(newSpaceLink.getId());
        Assert.assertEquals(mockSpaceLink.get().getId(), spaceLinkActual.getId());
    }

    @Test
    public void test_getLink_idNotExists() throws Exception {
        Mockito.when(spaceLinkRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getLink(Mockito.anyString()));
    }
}
