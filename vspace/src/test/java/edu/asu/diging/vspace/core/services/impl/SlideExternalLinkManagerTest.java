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
import edu.asu.diging.vspace.core.data.SlideExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISlideDisplay;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class SlideExternalLinkManagerTest {
    
    @Mock
    private SlideExternalLinkRepository externalLinkRepo;

    @Mock
    private SlideExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Mock
    private SlideManager slideManager;

    @Mock
    private SlideDisplayManager slideDisplayManager;

    @Mock
    private ISlideExternalLinkFactory externalLinkFactory;

    @Mock
    private ISlideExternalLinkDisplayFactory externalLinkDisplayFactory;

    @InjectMocks
    private SlideExternalLinkManager managerToTest = new SlideExternalLinkManager();

    private String slideId1;
    private String externalLinkURL;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        slideId1 = "SLA000000014";
        externalLinkURL = "www.google.com";
    }

    @Test
    public void test_createLink_success() throws SlideDoesNotExistException, ImageCouldNotBeStoredException {

        Slide slide = new Slide();
        slide.setId(slideId1);
        IVSImage slideImage = new VSImage();
        slideImage.setHeight(700);
        slideImage.setWidth(1300);

        ISlideDisplay displayAttributes = new SlideDisplay();
        displayAttributes.setHeight(700);
        displayAttributes.setWidth(1300);

        Mockito.when(slideDisplayManager.getBySlide(slide, slideImage)).thenReturn(displayAttributes);
        Mockito.when(slideManager.getSlide(slide.getId())).thenReturn(slide);

        IExternalLinkSlide externalLink = new ExternalLinkSlide();
        ExternalLinkValue externalLinkValue = new ExternalLinkValue(externalLinkURL);
        externalLink.setId("EXLS001");
        externalLink.setTarget(externalLinkValue);
        Mockito.when(slideManager.getSlide(slideId1)).thenReturn(slide);
        Mockito.when(externalLinkFactory.createExternalLink("New External Link", slide)).thenReturn(externalLink);

        Mockito.when(externalLinkRepo.save((ExternalLinkSlide) externalLink)).thenReturn((ExternalLinkSlide)externalLink);

        ISlideExternalLinkDisplay externalDisplayLink = new SlideExternalLinkDisplay();
        externalDisplayLink.setId("EXLDS001");

        externalDisplayLink.setPositionX(10);
        externalDisplayLink.setPositionY(30);
        externalDisplayLink.setRotation(40);
        externalDisplayLink.setType(DisplayType.ARROW);
        externalDisplayLink.setExternalLink(externalLink);
        Mockito.when(externalLinkDisplayFactory.createExternalLinkDisplay(externalLink)).thenReturn(externalDisplayLink);

        Mockito.when(externalLinkRepo.save((ExternalLinkSlide) externalLink)).thenReturn((ExternalLinkSlide)externalLink);
        Mockito.when(externalLinkDisplayRepo.save((SlideExternalLinkDisplay)externalDisplayLink)).thenReturn((SlideExternalLinkDisplay)externalDisplayLink);

        ISlideExternalLinkDisplay savedExternalLinkDisplay1 = managerToTest.createLink("New External Link", slideId1, 10, 30, 40, "EXLS001", "New External Link", DisplayType.ARROW, null, null,null);
        Assert.assertEquals(externalDisplayLink.getId(), savedExternalLinkDisplay1.getId());
        Assert.assertEquals(externalDisplayLink.getName(), savedExternalLinkDisplay1.getName());
        Assert.assertEquals(new Double(externalDisplayLink.getPositionX()), new Double(savedExternalLinkDisplay1.getPositionX()));
        Assert.assertEquals(new Double(externalDisplayLink.getPositionY()), new Double(savedExternalLinkDisplay1.getPositionY()));
        Assert.assertEquals(externalDisplayLink.getRotation(), savedExternalLinkDisplay1.getRotation());
        Assert.assertEquals(externalDisplayLink.getType(), savedExternalLinkDisplay1.getType());
        Assert.assertEquals(externalDisplayLink.getExternalLink(), savedExternalLinkDisplay1.getExternalLink());
        Assert.assertEquals(externalDisplayLink.getExternalLink().getTarget().getValue(), savedExternalLinkDisplay1.getExternalLink().getTarget().getValue());
        Mockito.verify(externalLinkDisplayRepo).save((SlideExternalLinkDisplay)externalDisplayLink);
    }
    
    @Test
    public void test_updateLink_success() throws SlideDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {
        ISlide slide = new Slide();
        slide.setId(slideId1);
        IVSImage slideImage = new VSImage();
        slideImage.setHeight(700);
        slideImage.setWidth(1300);
        slide.setImage(slideImage);

        ISlideDisplay displayAttributes = new SlideDisplay();
        displayAttributes.setHeight(700);
        displayAttributes.setWidth(1300);

        Mockito.when(slideDisplayManager.getBySlide(slide,slideImage)).thenReturn(displayAttributes);
        SlideExternalLinkDisplay externalLinkDisplay = new SlideExternalLinkDisplay();
        IExternalLinkSlide externalLink = new ExternalLinkSlide();
        externalLink.setId("EXLS001");
        externalLink.setSlide(slide);
        externalLink.setTarget(new ExternalLinkValue("stackOverflow.com"));
        externalLinkDisplay.setId("EXLDS001");

        externalLinkDisplay.setExternalLink(externalLink);
        
        externalLinkDisplay.setName("TestExternalEdited");
        externalLinkDisplay.setPositionX(100);
        externalLinkDisplay.setPositionY(300);
        externalLinkDisplay.setType(DisplayType.ALERT);

        Mockito.when(slideManager.getSpace(slideId1)).thenReturn(slide);
        ExternalLink newExternalLink = new ExternalLink(); 
        newExternalLink.setId("externalLink1");
        Optional<ExternalLinkSlide> mockExternalLink = Optional.of((ExternalLinkSlide)externalLink);
        Mockito.when(externalLinkRepo.findById(externalLink.getId())).thenReturn(mockExternalLink);

        Optional<SlideExternalLinkDisplay> mockExternalLinkDisplay = Optional.of((SlideExternalLinkDisplay)externalLinkDisplay);
        Mockito.when(externalLinkDisplayRepo.findById(externalLinkDisplay.getId())).thenReturn(mockExternalLinkDisplay);

        Mockito.when(externalLinkRepo.save((ExternalLinkSlide) externalLink)).thenReturn((ExternalLinkSlide)externalLink);
        Mockito.when(externalLinkDisplayRepo.save((SlideExternalLinkDisplay)externalLinkDisplay)).thenReturn((SlideExternalLinkDisplay)externalLinkDisplay);

        ISlideExternalLinkDisplay actualUpdatedLink = managerToTest.updateLink("TestExternalNew", slideId1, 20, 40, 0, "www.google.com", "TestExternalNew", "EXLS001", "EXLDS001", DisplayType.ARROW, null, null,null);
        Assert.assertEquals(externalLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(externalLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(externalLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getId(), actualUpdatedLink.getExternalLink().getId());
        Assert.assertEquals(externalLinkDisplay.getExternalLink().getTarget().getValue(), actualUpdatedLink.getExternalLink().getTarget().getValue());
        Assert.assertEquals(externalLinkDisplay.getType(), actualUpdatedLink.getType());
    }
    
    

}
