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

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.factory.impl.SpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

public class SpaceLinkManagerTest {
    @Mock
    private SpaceLinkRepository spaceLinkRepo;

    @Mock
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;
    
    @Mock
    private SpaceManager spaceManager;
    
    @Mock
    private ISpaceLinkFactory spaceLinkFactory;
    
    @Mock
    private SpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @InjectMocks
    private SpaceLinkManager managerToTest = new SpaceLinkManager();
    
    private String spaceId1, spaceId2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000014";
        spaceId2 = "SPA000000015";
    }
    
    @Test
    public void test_createLink_success() throws SpaceDoesNotExistException, ImageCouldNotBeStoredException {
        
        ISpace space = new Space();
        space.setId(spaceId1);
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        ISpaceLink spaceLink = new SpaceLink();
        spaceLink.setId("SPL001");
        Mockito.when(spaceLinkRepo.save((SpaceLink)spaceLink)).thenReturn((SpaceLink) spaceLink);
        Mockito.when(managerToTest.createLinkObject("New Space Link", spaceId1)).thenReturn(spaceLink);
        
        ISpace target = new Space();
        target.setId(spaceId2);
        Mockito.when(spaceManager.getSpace(spaceId2)).thenReturn(target);
        Mockito.when(managerToTest.getTarget(spaceId2)).thenReturn(target);
        
        spaceLink.setTargetSpace(target);
        ISpaceLinkDisplay spaceDisplayLink = new SpaceLinkDisplay();
        spaceDisplayLink.setId("SPLD001");
        Mockito.when(managerToTest.createDisplayLink(spaceLink)).thenReturn(spaceDisplayLink);
        
        spaceDisplayLink.setPositionX(10);
        spaceDisplayLink.setPositionY(30);
        spaceDisplayLink.setRotation(40);
        spaceDisplayLink.setType(DisplayType.ARROW);
        spaceDisplayLink.setLink(spaceLink);
        
        Mockito.when(managerToTest.updateLinkAndDisplay(spaceLink, spaceDisplayLink)).thenReturn(spaceDisplayLink);
        
        ISpaceLinkDisplay savedSpaceLinkDisplay = managerToTest.createLink("New Space Link", spaceId1, 10, 30, 40, spaceId2, "New Space Link", DisplayType.ARROW, null, null);
        Assert.assertEquals(spaceDisplayLink.getId(), savedSpaceLinkDisplay.getId());
        Assert.assertEquals(spaceDisplayLink.getName(), savedSpaceLinkDisplay.getName());
        Assert.assertEquals(new Double(spaceDisplayLink.getPositionX()), new Double(savedSpaceLinkDisplay.getPositionX()));
        Assert.assertEquals(new Double(spaceDisplayLink.getPositionY()), new Double(savedSpaceLinkDisplay.getPositionY()));
        Assert.assertEquals(spaceDisplayLink.getRotation(), savedSpaceLinkDisplay.getRotation());
        Assert.assertEquals(spaceDisplayLink.getLink().getId(), savedSpaceLinkDisplay.getLink().getId());
        Assert.assertEquals(spaceDisplayLink.getType(), savedSpaceLinkDisplay.getType());
        Assert.assertEquals(spaceDisplayLink.getLink().getTargetSpace(), savedSpaceLinkDisplay.getLink().getTargetSpace());
        Mockito.verify(spaceLinkDisplayRepo).save((SpaceLinkDisplay)spaceDisplayLink);
    }

    @Test
    public void test_getDisplayLink_idExists() {
        SpaceLinkDisplay newSpaceLinkDisplay = new SpaceLinkDisplay();
        newSpaceLinkDisplay.setId("SPLD001");
        Optional<SpaceLinkDisplay> mockSpaceLinkDisplay = Optional.of(newSpaceLinkDisplay);
        Mockito.when(spaceLinkDisplayRepo.findById(newSpaceLinkDisplay.getId())).thenReturn(mockSpaceLinkDisplay);

        ISpaceLinkDisplay spaceLinkDisplayActual = managerToTest.getDisplayLink(newSpaceLinkDisplay.getId());
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

    @Test
    public void test_deleteLinkDisplayRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        List<ISpaceLinkDisplay> SpaceLinkDisplayList = new ArrayList<ISpaceLinkDisplay>();
        SpaceLinkDisplay SpaceLinkDisplay = new SpaceLinkDisplay();
        ISpaceLink spaceLink = new SpaceLink();
        spaceLink.setId("SPL001");
        spaceLink.setSpace(space);
        SpaceLinkDisplay.setLink(spaceLink);
        SpaceLinkDisplayList.add(SpaceLinkDisplay);
        SpaceLinkDisplay = new SpaceLinkDisplay();
        spaceLink = new SpaceLink();
        spaceLink.setId("SPL002");
        spaceLink.setSpace(space);
        SpaceLinkDisplay.setLink(spaceLink);
        SpaceLinkDisplayList.add(SpaceLinkDisplay);
        List<ISpaceLinkDisplay> spaceLinkObj = SpaceLinkDisplayList;
        Mockito.when(spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(space.getId())).thenReturn(spaceLinkObj);
        managerToTest.deleteLinkDisplayRepo(spaceLink);
        Mockito.verify(spaceLinkDisplayRepo).deleteByLink(spaceLink);
    }

    @Test
    public void test_deleteLinkRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        SpaceLink spaceLink = new SpaceLink();
        spaceLink.setId("SPL001");
        spaceLink.setSpace(space);
        Mockito.when(spaceLinkRepo.save(spaceLink)).thenReturn(spaceLink);
        managerToTest.deleteLinkRepo(spaceLink);
        Mockito.verify(spaceLinkRepo).delete(spaceLink);
    }

    @Test
    public void test_updateDisplayLink_success() {
        ISpace space = new Space();
        space.setId("SPA001");
        SpaceLinkDisplay spaceLinkDisplay = new SpaceLinkDisplay();
        ISpaceLink spaceLink = new SpaceLink();
        spaceLink.setId("SPL001");
        spaceLink.setSpace(space);
        spaceLinkDisplay.setName("TestSpace");
        spaceLinkDisplay.setPositionX(10);
        spaceLinkDisplay.setPositionY(30);
        spaceLinkDisplay.setRotation(20);
        spaceLinkDisplay.setType(DisplayType.ARROW);
        spaceLinkDisplay.setLink(spaceLink);
        Mockito.when(spaceLinkDisplayRepo.save(spaceLinkDisplay)).thenReturn(spaceLinkDisplay);

        spaceLinkDisplay.setName("TestSpaceEdited");
        spaceLinkDisplay.setPositionX(100);
        spaceLinkDisplay.setPositionY(300);
        spaceLinkDisplay.setRotation(200);
        spaceLinkDisplay.setType(DisplayType.ALERT);

        ISpaceLinkDisplay actualUpdatedLink = managerToTest.updateLinkAndDisplay(spaceLink, spaceLinkDisplay);
        Assert.assertEquals(spaceLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(spaceLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(spaceLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(spaceLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(spaceLinkDisplay.getRotation(), actualUpdatedLink.getRotation());
        Assert.assertEquals(spaceLinkDisplay.getLink().getId(), actualUpdatedLink.getLink().getId());
        Assert.assertEquals(spaceLinkDisplay.getType(), actualUpdatedLink.getType());
    }
}
