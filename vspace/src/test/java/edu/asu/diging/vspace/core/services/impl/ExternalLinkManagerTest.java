package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
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
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.Space;

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

        IExternalLinkDisplay externalLinkDisplayActual = managerToTest.getDisplayLink(newExternalLinkDisplay.getId());
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

    @Test
    public void test_deleteLinkDisplayRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        List<IExternalLinkDisplay> externalLinkDisplayList = new ArrayList<IExternalLinkDisplay>();
        ExternalLinkDisplay externalLinkDisplay = new ExternalLinkDisplay();
        IExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);
        externalLinkDisplay.setExternalLink(externalLink);
        externalLinkDisplayList.add(externalLinkDisplay);
        externalLinkDisplay = new ExternalLinkDisplay();
        externalLink = new ExternalLink();
        externalLink.setId("EXL002");
        externalLink.setSpace(space);
        externalLinkDisplay.setExternalLink(externalLink);
        externalLinkDisplayList.add(externalLinkDisplay);
        List<IExternalLinkDisplay> externalLinkObj = externalLinkDisplayList;
        Mockito.when(externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(space.getId())).thenReturn(externalLinkObj);
        managerToTest.deleteLinkDisplayRepo(externalLink);
        Mockito.verify(externalLinkDisplayRepo).deleteByExternalLink(externalLink);
    }

    @Test
    public void test_deleteLinkRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        ExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);
        Mockito.when(externalLinkRepo.save(externalLink)).thenReturn(externalLink);
        managerToTest.deleteLinkRepo(externalLink);
        Mockito.verify(externalLinkRepo).delete(externalLink);
    }

    @Test
    public void test_updateDisplayLink_success() {
        ISpace space = new Space();
        space.setId("SPA001");
        ExternalLinkDisplay externalLinkDisplay = new ExternalLinkDisplay();
        IExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);
        externalLinkDisplay.setName("TestExternal");
        externalLinkDisplay.setPositionX(10);
        externalLinkDisplay.setPositionY(30);
        externalLinkDisplay.setType(DisplayType.ARROW);
        externalLinkDisplay.setExternalLink(externalLink);
        Mockito.when(externalLinkDisplayRepo.save(externalLinkDisplay)).thenReturn(externalLinkDisplay);

        externalLinkDisplay.setName("TestExternalEdited");
        externalLinkDisplay.setPositionX(100);
        externalLinkDisplay.setPositionY(300);
        externalLinkDisplay.setType(DisplayType.ALERT);

        IExternalLinkDisplay actualUpdatedLink = managerToTest.updateLinkAndDisplay(externalLink, externalLinkDisplay);
        Assert.assertEquals(externalLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(externalLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getId(), actualUpdatedLink.getExternalLink().getId());
        Assert.assertEquals(externalLinkDisplay.getType(), actualUpdatedLink.getType());
    }
}
