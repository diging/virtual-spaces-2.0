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

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class ModuleLinkManagerTest {
    @Mock
    private ModuleLinkRepository moduleLinkRepo;

    @Mock
    private ModuleLinkDisplayRepository moduleLinkDisplayRepo;

    @Mock
    private SpaceManager spaceManager;

    @Mock
    private SpaceDisplayManager spaceDisplayManager;

    @Mock
    private ModuleManager moduleManager;

    @Mock
    private IModuleLinkFactory moduleLinkFactory;
    
    @Mock
    private IImageFactory imageFactory;
    
    @Mock
    private ImageRepository imageRepo;
    
    @Mock
    private IStorageEngine storage;

    @Mock
    private IModuleLinkDisplayFactory moduleLinkDisplayFactory;

    @InjectMocks
    private ModuleLinkManager managerToTest = new ModuleLinkManager();

    private String spaceId1, imageId, imageFileName, modDisplayLinkId;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000014";
        imageId = "IMG00000001";
        imageFileName = "Module Image 1";
        modDisplayLinkId = "MDLD001";
    }

    @Test
    public void test_createLink_success() throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, FileStorageException, ImageDoesNotExistException {

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

        IModuleLink moduleLink = new ModuleLink();
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        Mockito.when(moduleLinkFactory.createModuleLink("New Module Link", space)).thenReturn(moduleLink);

        IModule target = new Module();
        target.setId("MOD001");
        Mockito.when(moduleManager.getModule("MOD001")).thenReturn(target);

        moduleLink.setTarget(target);

        IModuleLinkDisplay moduleDisplayLink = new ModuleLinkDisplay();
        moduleDisplayLink.setId(modDisplayLinkId);
        moduleDisplayLink.setPositionX(10);
        moduleDisplayLink.setPositionY(30);
        moduleDisplayLink.setRotation(40);
        moduleDisplayLink.setType(DisplayType.ARROW);
        Mockito.when(moduleLinkFactory.createModuleLink("New Module Link", space)).thenReturn(moduleLink);
        moduleDisplayLink.setLink(moduleLink);

        Mockito.when(moduleLinkDisplayFactory.createModuleLinkDisplay(moduleLink)).thenReturn(moduleDisplayLink);

        Mockito.when(moduleLinkRepo.save((ModuleLink) moduleLink)).thenReturn((ModuleLink)moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save((ModuleLinkDisplay)moduleDisplayLink)).thenReturn((ModuleLinkDisplay)moduleDisplayLink);

        IModuleLinkDisplay savedModuleLinkDisplay1 = managerToTest.createLink("New Module Link", spaceId1, 10, 30, 40, "MOD001", "New Module Link", DisplayType.ARROW, null, null, null);
        Assert.assertEquals(moduleDisplayLink.getId(), savedModuleLinkDisplay1.getId());
        Assert.assertEquals(moduleDisplayLink.getName(), savedModuleLinkDisplay1.getName());
        Assert.assertEquals(new Double(moduleDisplayLink.getPositionX()), new Double(savedModuleLinkDisplay1.getPositionX()));
        Assert.assertEquals(new Double(moduleDisplayLink.getPositionY()), new Double(savedModuleLinkDisplay1.getPositionY()));
        Assert.assertEquals(moduleDisplayLink.getRotation(), savedModuleLinkDisplay1.getRotation());
        Assert.assertEquals(moduleDisplayLink.getLink().getId(), savedModuleLinkDisplay1.getLink().getId());
        Assert.assertEquals(moduleDisplayLink.getType(), savedModuleLinkDisplay1.getType());
        Assert.assertEquals(moduleDisplayLink.getLink().getTarget().getId(), savedModuleLinkDisplay1.getLink().getTarget().getId());
        Mockito.verify(moduleLinkDisplayRepo).save((ModuleLinkDisplay)moduleDisplayLink);
        
        
        //For moduleLink as Image 
        IVSImage modImage = new VSImage();
        modImage.setId(imageId);
        modImage.setFilename(imageFileName);
        modImage.setHeight(200);
        modImage.setWidth(400);
        
        IModuleLinkDisplay moduleDisplayLinkImage = new ModuleLinkDisplay();
        moduleDisplayLinkImage.setId(modDisplayLinkId);
        moduleDisplayLinkImage.setPositionX(10);
        moduleDisplayLinkImage.setPositionY(30);
        moduleDisplayLinkImage.setRotation(40);
        moduleDisplayLinkImage.setType(DisplayType.IMAGE);
        moduleDisplayLinkImage.setImage(modImage);
        Mockito.when(moduleLinkFactory.createModuleLink("New Module Link", space)).thenReturn(moduleLink);
        moduleDisplayLinkImage.setLink(moduleLink);
        
        Mockito.when(moduleLinkDisplayFactory.createModuleLinkDisplay(moduleLink)).thenReturn(moduleDisplayLinkImage);
        
        Mockito.when(imageFactory.createImage(Mockito.anyString(), Mockito.anyString())).thenReturn(modImage);
        Mockito.when(imageRepo.save((VSImage) modImage)).thenReturn((VSImage) modImage);
        Mockito.when(storage.storeFile(new byte[20], imageFileName, modImage.getId())).thenReturn("Dummy File Path");
        
        Mockito.when(moduleLinkRepo.save((ModuleLink) moduleLink)).thenReturn((ModuleLink)moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save((ModuleLinkDisplay)moduleDisplayLinkImage)).thenReturn((ModuleLinkDisplay)moduleDisplayLinkImage);

        IModuleLinkDisplay savedModuleLinkDisplay2 = managerToTest.createLink("New Module Link", spaceId1, 10, 30, 40, "MOD001", "New Module Link", DisplayType.IMAGE, new byte[20], imageFileName, null);
        Assert.assertEquals(moduleDisplayLinkImage.getId(), savedModuleLinkDisplay2.getId());
        Assert.assertEquals(moduleDisplayLinkImage.getName(), savedModuleLinkDisplay2.getName());
        Assert.assertEquals(new Double(moduleDisplayLinkImage.getPositionX()), new Double(savedModuleLinkDisplay2.getPositionX()));
        Assert.assertEquals(new Double(moduleDisplayLinkImage.getPositionY()), new Double(savedModuleLinkDisplay2.getPositionY()));
        Assert.assertEquals(moduleDisplayLinkImage.getRotation(), savedModuleLinkDisplay2.getRotation());
        Assert.assertEquals(moduleDisplayLinkImage.getLink().getId(), savedModuleLinkDisplay2.getLink().getId());
        Assert.assertEquals(moduleDisplayLinkImage.getType(), savedModuleLinkDisplay2.getType());
        Assert.assertEquals(moduleDisplayLinkImage.getLink().getTarget().getId(), savedModuleLinkDisplay2.getLink().getTarget().getId());
        Assert.assertEquals(moduleDisplayLinkImage.getImage().getHeight(), savedModuleLinkDisplay2.getImage().getHeight());
        Assert.assertEquals(moduleDisplayLinkImage.getImage().getWidth(), savedModuleLinkDisplay2.getImage().getWidth());
        Assert.assertEquals(moduleDisplayLinkImage.getImage().getId(), savedModuleLinkDisplay2.getImage().getId());
        Mockito.verify(moduleLinkDisplayRepo).save((ModuleLinkDisplay)moduleDisplayLinkImage);
        
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
        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);

        IModule target = new Module();
        target.setId("MOD001");
        Mockito.when(moduleManager.getModule("MOD001")).thenReturn(target);

        moduleLink.setTarget(target);
        moduleLinkDisplay.setId(modDisplayLinkId);
        moduleLinkDisplay.setLink(moduleLink);

        moduleLinkDisplay.setName("TestModule");
        moduleLinkDisplay.setPositionX(10);
        moduleLinkDisplay.setPositionY(30);
        moduleLinkDisplay.setPositionY(20);
        moduleLinkDisplay.setType(DisplayType.ARROW);

        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        ModuleLink newModuleLink = new ModuleLink();
        newModuleLink.setId("moduleLink1");
        Optional<ModuleLink> mockModuleLink = Optional.of((ModuleLink)moduleLink);
        Mockito.when(moduleLinkRepo.findById(moduleLink.getId())).thenReturn(mockModuleLink);

        Optional<ModuleLinkDisplay> mockModuleLinkDisplay = Optional.of((ModuleLinkDisplay)moduleLinkDisplay);
        Mockito.when(moduleLinkDisplayRepo.findById(moduleLinkDisplay.getId())).thenReturn(mockModuleLinkDisplay);


        ModuleLinkDisplay moduleLinkDisplayUpdated = moduleLinkDisplay;
        moduleLinkDisplayUpdated.setName("TestModuleEdited");
        moduleLinkDisplayUpdated.setPositionX(100);
        moduleLinkDisplayUpdated.setPositionY(300);
        moduleLinkDisplayUpdated.setRotation(180);
        moduleLinkDisplayUpdated.setType(DisplayType.ALERT);

        IModule newTarget = new Module();
        newTarget.setId("MOD002");
        Mockito.when(moduleManager.getModule("MOD002")).thenReturn(newTarget);
        moduleLinkDisplayUpdated.getLink().setTarget(newTarget);

        Mockito.when(moduleLinkRepo.save((ModuleLink) moduleLink)).thenReturn((ModuleLink)moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save((ModuleLinkDisplay)moduleLinkDisplay)).thenReturn((ModuleLinkDisplay)moduleLinkDisplayUpdated);

        IModuleLinkDisplay actualUpdatedLink = managerToTest.updateLink("TestModuleEdited", spaceId1, 100, 300, 180, "MOD002", "TestModuleEdited", "MOL001", modDisplayLinkId, DisplayType.ALERT, null, null, null);
        Assert.assertEquals(moduleLinkDisplayUpdated.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(moduleLinkDisplayUpdated.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(moduleLinkDisplayUpdated.getLink().getTarget(), actualUpdatedLink.getLink().getTarget());
        Assert.assertEquals(moduleLinkDisplayUpdated.getType(), actualUpdatedLink.getType());
        
        //For moduleLink as Image
        IVSImage modImage = new VSImage();
        modImage.setId(imageId);
        modImage.setFilename(imageFileName);
        modImage.setHeight(200);
        modImage.setWidth(400);
        
        moduleLinkDisplay.setType(DisplayType.IMAGE);
        moduleLinkDisplay.setImage(modImage);
        moduleLinkDisplayUpdated.setType(DisplayType.ARROW);
        
        IModuleLinkDisplay actualUpdatedLink2 = managerToTest.updateLink("TestModuleEdited", spaceId1, 100, 300, 180, "MOD002", "TestModuleEdited", "MOL001", modDisplayLinkId, DisplayType.ALERT, null, null, null);
        Assert.assertEquals(moduleLinkDisplayUpdated.getId(), actualUpdatedLink2.getId());
        Assert.assertEquals(moduleLinkDisplayUpdated.getName(), actualUpdatedLink2.getName());
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionX()), new Double(actualUpdatedLink2.getPositionX()));
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionY()), new Double(actualUpdatedLink2.getPositionY()));
        Assert.assertEquals(moduleLinkDisplayUpdated.getLink().getTarget(), actualUpdatedLink2.getLink().getTarget());
        Assert.assertEquals(moduleLinkDisplayUpdated.getType(), actualUpdatedLink2.getType());
    }

    @Test
    public void test_deleteLink_linkPresent() {
        ISpace space = new Space();
        space.setId("SPA001");
        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL005");
        moduleLink.setSpace(space);

        List<IModuleLink> moduleLinks = new ArrayList<IModuleLink>();
        moduleLinks.add(moduleLink);

        space.setModuleLinks(moduleLinks);
        IModule target = new Module();
        target.setId("MOD005");
        moduleLink.setTarget(target);

        moduleLinkDisplay.setId(modDisplayLinkId);
        moduleLinkDisplay.setLink(moduleLink);

        moduleLinkDisplay.setName("TestModule");
        moduleLinkDisplay.setPositionX(10);
        moduleLinkDisplay.setPositionY(30);
        moduleLinkDisplay.setPositionY(20);
        moduleLinkDisplay.setType(DisplayType.ARROW);

        Optional<ModuleLink> mockModuleLink = Optional.of((ModuleLink)moduleLink);
        Mockito.when(moduleLinkRepo.findById(moduleLink.getId())).thenReturn(mockModuleLink);

        Optional<ModuleLinkDisplay> mockModuleLinkDisplay = Optional.of((ModuleLinkDisplay)moduleLinkDisplay);
        Mockito.when(moduleLinkDisplayRepo.findById(moduleLinkDisplay.getId())).thenReturn(mockModuleLinkDisplay);

        managerToTest.deleteLink("MOL005");

        Mockito.verify(moduleLinkDisplayRepo).deleteByLink(moduleLink);
        Mockito.verify(moduleLinkRepo).delete((ModuleLink)moduleLink);
    }
}
