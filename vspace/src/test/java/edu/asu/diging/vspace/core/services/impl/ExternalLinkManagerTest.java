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
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class ExternalLinkManagerTest {

    @Mock
    private ExternalLinkRepository externalLinkRepo;

    @Mock
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Mock
    private SpaceManager spaceManager;

    @Mock
    private SpaceDisplayManager spaceDisplayManager;

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
    public void test_createLink_success() throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, ImageDoesNotExistException {

        Space space = new Space();
        space.setId(spaceId1);
        IVSImage spaceImage = new VSImage();
        spaceImage.setHeight(700);
        spaceImage.setWidth(1300);
        space.setImage(spaceImage);

        ISpaceDisplay displayAttributes = new SpaceDisplay();
        displayAttributes.setHeight(700);
        displayAttributes.setWidth(1300);

        Mockito.when(spaceDisplayManager.getBySpace(space)).thenReturn(displayAttributes);
        Mockito.when(spaceManager.getSpace(space.getId())).thenReturn(space);

        IExternalLink externalLink = new ExternalLink();
        ExternalLinkValue externalLinkValue = new ExternalLinkValue(externalLinkURL);
        externalLink.setId("EXL001");
        externalLink.setTarget(externalLinkValue);
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        Mockito.when(externalLinkFactory.createExternalLink("New External Link", space)).thenReturn(externalLink);

        Mockito.when(externalLinkRepo.save((ExternalLink) externalLink)).thenReturn((ExternalLink)externalLink);

        IExternalLinkDisplay externalDisplayLink = new ExternalLinkDisplay();
        externalDisplayLink.setId("EXLD001");

        externalDisplayLink.setPositionX(10);
        externalDisplayLink.setPositionY(30);
        externalDisplayLink.setRotation(40);
        externalDisplayLink.setType(DisplayType.ARROW);
        externalDisplayLink.setExternalLink(externalLink);
        Mockito.when(externalLinkDisplayFactory.createExternalLinkDisplay(externalLink)).thenReturn(externalDisplayLink);

        Mockito.when(externalLinkRepo.save((ExternalLink) externalLink)).thenReturn((ExternalLink)externalLink);
        Mockito.when(externalLinkDisplayRepo.save((ExternalLinkDisplay)externalDisplayLink)).thenReturn((ExternalLinkDisplay)externalDisplayLink);

        IExternalLinkDisplay savedExternalLinkDisplay1 = managerToTest.createLink("New External Link","EXL001", 10.0f, 30.0f, 40, "EXL001", "New External Link", "Description", DisplayType.ARROW, null, null, null, "IMG000000001");
        Assert.assertEquals(externalDisplayLink.getId(), savedExternalLinkDisplay1.getId());
        Assert.assertEquals(externalDisplayLink.getName(), savedExternalLinkDisplay1.getName());
        Assert.assertEquals(new Double(externalDisplayLink.getPositionX()), new Double(savedExternalLinkDisplay1.getPositionX()));
        Assert.assertEquals(new Double(externalDisplayLink.getPositionY()), new Double(savedExternalLinkDisplay1.getPositionY()));
        Assert.assertEquals(externalDisplayLink.getRotation(), savedExternalLinkDisplay1.getRotation());
        Assert.assertEquals(externalDisplayLink.getType(), savedExternalLinkDisplay1.getType());
        Assert.assertEquals(externalDisplayLink.getExternalLink(), savedExternalLinkDisplay1.getExternalLink());
        Assert.assertEquals(externalDisplayLink.getExternalLink().getTarget().getValue(), savedExternalLinkDisplay1.getExternalLink().getTarget().getValue());
        Mockito.verify(externalLinkDisplayRepo).save((ExternalLinkDisplay)externalDisplayLink);
    }

    @Test
    public void test_updateLink_success() throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException, ImageDoesNotExistException {
        ISpace space = new Space();
        space.setId(spaceId1);
        IVSImage spaceImage = new VSImage();
        spaceImage.setHeight(700);
        spaceImage.setWidth(1300);
        space.setImage(spaceImage);

        ISpaceDisplay displayAttributes = new SpaceDisplay();
        displayAttributes.setHeight(700);
        displayAttributes.setWidth(1300);

        Mockito.when(spaceDisplayManager.getBySpace(space)).thenReturn(displayAttributes);
        ExternalLinkDisplay externalLinkDisplay = new ExternalLinkDisplay();
        IExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);
        externalLink.setTarget(new ExternalLinkValue("stackOverflow.com"));
        externalLinkDisplay.setId("EXLD001");

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

        Mockito.when(externalLinkRepo.save((ExternalLink) externalLink)).thenReturn((ExternalLink)externalLink);
        Mockito.when(externalLinkDisplayRepo.save((ExternalLinkDisplay)externalLinkDisplay)).thenReturn((ExternalLinkDisplay)externalLinkDisplay);

        IExternalLinkDisplay actualUpdatedLink = managerToTest.updateLink("TestExternalNew", spaceId1, 20.0f, 40.0f, 0, "www.google.com", "TestExternalNew", "Description", "EXL001", "EXLD001", DisplayType.ARROW, null, null, null, null);
        Assert.assertEquals(externalLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(externalLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getId(), actualUpdatedLink.getExternalLink().getId());
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getTarget().getValue(), actualUpdatedLink.getExternalLink().getTarget().getValue());
        Assert.assertEquals(externalLinkDisplay.getType(), actualUpdatedLink.getType());
    }

    @Test
    public void test_deleteLink_linkPresent() {
        ISpace space = new Space();
        space.setId(spaceId1);
        ExternalLinkDisplay externalLinkDisplay = new ExternalLinkDisplay();
        IExternalLink externalLink = new ExternalLink();
        externalLink.setId("EXL001");
        externalLink.setSpace(space);

        List<IExternalLink> externalLinks = new ArrayList<IExternalLink>();
        externalLinks.add(externalLink);

        space.setExternalLinks(externalLinks);
        ExternalLinkValue target = new ExternalLinkValue("www.google.com");
        target.setId("EXT001");
        externalLink.setTarget(target);

        externalLinkDisplay.setId("SPLD001");
        externalLinkDisplay.setExternalLink(externalLink);

        externalLinkDisplay.setName("TestExternal");
        externalLinkDisplay.setPositionX(10);
        externalLinkDisplay.setPositionY(30);
        externalLinkDisplay.setPositionY(20);
        externalLinkDisplay.setType(DisplayType.ARROW);

        Optional<ExternalLink> mockExternalLink = Optional.of((ExternalLink)externalLink);
        Mockito.when(externalLinkRepo.findById(externalLink.getId())).thenReturn(mockExternalLink);

        Optional<ExternalLinkDisplay> mockExternalLinkDisplay = Optional.of((ExternalLinkDisplay)externalLinkDisplay);
        Mockito.when(externalLinkDisplayRepo.findById(externalLinkDisplay.getId())).thenReturn(mockExternalLinkDisplay);

        managerToTest.deleteLink("EXL001");

        Mockito.verify(externalLinkDisplayRepo).deleteByExternalLink(externalLink);
        Mockito.verify(externalLinkRepo).delete((ExternalLink)externalLink);
    }
}
