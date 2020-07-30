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
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.model.impl.Space;

public class ExternalLinkManagerTest {
    
    @Mock
    private ExternalLinkRepository externalLinkRepo;

    @Mock
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;
    
    @Mock
    private SpaceManager spaceManager;
    
    @Mock
    private IExternalLinkFactory externalLinkFactory;
    
    @Mock
    private IExternalLinkDisplayFactory externalLinkDisplayFactory;

    @InjectMocks
    private ExternalLinkManager managerToTest = new ExternalLinkManager();
    
    private String spaceId1;
    private String externalLinkURL;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000014";
        externalLinkURL = "www.google.com";
    }
    
    @Test
    public void test_createLink_success() throws SpaceDoesNotExistException, ImageCouldNotBeStoredException {
        
        Space space = new Space();
        space.setId(spaceId1);
        Mockito.when(spaceManager.getSpace(space.getId())).thenReturn(space);
        
        IExternalLink externalLink = new ExternalLink();
        ExternalLinkValue externalLinkValue = new ExternalLinkValue(externalLinkURL);
        externalLink.setId("EXL001");
        externalLink.setTarget(externalLinkValue);
        Mockito.when(managerToTest.createLinkObject("New External Link", spaceId1)).thenReturn(externalLink);
        
        IExternalLinkDisplay externalDisplayLink = new ExternalLinkDisplay();
        externalDisplayLink.setId("EXLD001");
        Mockito.when(managerToTest.createDisplayLink(externalLink)).thenReturn(externalDisplayLink);
        externalDisplayLink.setPositionX(10);
        externalDisplayLink.setPositionY(30);
        externalDisplayLink.setRotation(40);
        externalDisplayLink.setType(DisplayType.ARROW);
        externalDisplayLink.setExternalLink(externalLink);
        
        Mockito.when(managerToTest.updateLinkAndDisplay(externalLink, externalDisplayLink)).thenReturn(externalDisplayLink);
        
        IExternalLinkDisplay savedExternalLinkDisplay1 = managerToTest.createLink("New External Link", spaceId1, 10, 30, 40, "EXL001", "New External Link", DisplayType.ARROW, null, null);
        Assert.assertEquals(externalDisplayLink.getId(), savedExternalLinkDisplay1.getId());
        Assert.assertEquals(externalDisplayLink.getName(), savedExternalLinkDisplay1.getName());
        Assert.assertEquals(new Double(externalDisplayLink.getPositionX()), new Double(savedExternalLinkDisplay1.getPositionX()));
        Assert.assertEquals(new Double(externalDisplayLink.getPositionY()), new Double(savedExternalLinkDisplay1.getPositionY()));
        Assert.assertEquals(externalDisplayLink.getRotation(), savedExternalLinkDisplay1.getRotation());
        Assert.assertEquals(externalDisplayLink.getType(), savedExternalLinkDisplay1.getType());
        Assert.assertEquals(externalDisplayLink.getExternalLink(), savedExternalLinkDisplay1.getExternalLink());
        Mockito.verify(externalLinkDisplayRepo).save((ExternalLinkDisplay)externalDisplayLink);
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
        newExternalLink.setId("EXL001");
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
    public void test_updateDisplayLink_success() throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {
        ISpace space = new Space();
        space.setId(spaceId1);
        ExternalLinkDisplay externalLinkDisplay = new ExternalLinkDisplay();
        IExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);
        externalLink.setTarget(new ExternalLinkValue("stackOverflow.com"));
        externalLinkDisplay.setId("EXLD001");
        externalLinkDisplay.setName("TestExternal");
        externalLinkDisplay.setPositionX(10);
        externalLinkDisplay.setPositionY(30);
        externalLinkDisplay.setType(DisplayType.ARROW);
        externalLinkDisplay.setExternalLink(externalLink);

        externalLinkDisplay.setName("TestExternalEdited");
        externalLinkDisplay.setPositionX(100);
        externalLinkDisplay.setPositionY(300);
        externalLinkDisplay.setType(DisplayType.ALERT);
        
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        ExternalLink newExternalLink = new ExternalLink(); 
        newExternalLink.setId("externalLink1");
        Optional<ExternalLink> mockExternalLink = Optional.of((ExternalLink)externalLink);
        Mockito.when(externalLinkRepo.findById(externalLink.getId())).thenReturn(mockExternalLink);
        
        Optional<ExternalLinkDisplay> mockExternalLinkDisplay = Optional.of((ExternalLinkDisplay)externalLinkDisplay);
        Mockito.when(externalLinkDisplayRepo.findById(externalLinkDisplay.getId())).thenReturn(mockExternalLinkDisplay);
        
        Mockito.when(managerToTest.updateLinkAndDisplay(externalLink, externalLinkDisplay)).thenReturn(externalLinkDisplay);
        
        IExternalLinkDisplay actualUpdatedLink = managerToTest.updateLink("TestExternalNew", spaceId1, 20, 40, 0, "www.google.com", "TestExternalNew", "EXL001", "EXLD001", DisplayType.ARROW, null, null);
        Assert.assertEquals(externalLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(externalLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getId(), actualUpdatedLink.getExternalLink().getId());
        Assert.assertEquals(externalLinkDisplay.getType(), actualUpdatedLink.getType());
    }
}
