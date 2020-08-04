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

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;

public class ModuleLinkManagerTest {
    @Mock
    private ModuleLinkRepository moduleLinkRepo;

    @Mock
    private ModuleLinkDisplayRepository moduleLinkDisplayRepo;

    @Mock
    private SpaceManager spaceManager;

    @Mock
    private ModuleManager moduleManager;

    @Mock
    private IModuleLinkFactory moduleLinkFactory;

    @Mock
    private IModuleLinkDisplayFactory moduleLinkDisplayFactory;

    @InjectMocks
    private ModuleLinkManager managerToTest = new ModuleLinkManager();

    private String spaceId1;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000014";
    }

    @Test
    public void test_createLink_success() throws SpaceDoesNotExistException, ImageCouldNotBeStoredException {

        Space space = new Space();
        space.setId(spaceId1);
        Mockito.when(spaceManager.getSpace(space.getId())).thenReturn(space);

        IModuleLink moduleLink = new ModuleLink();
        Mockito.when(spaceManager.getSpace(spaceId1)).thenReturn(space);
        Mockito.when(moduleLinkFactory.createModuleLink("New Module Link", space)).thenReturn(moduleLink);

        IModule target = new Module();
        target.setId("MOD001");
        Mockito.when(moduleManager.getModule("MOD001")).thenReturn(target);

        moduleLink.setTarget(target);

        IModuleLinkDisplay moduleDisplayLink = new ModuleLinkDisplay();
        moduleDisplayLink.setId("MDLD001");
        moduleDisplayLink.setPositionX(10);
        moduleDisplayLink.setPositionY(30);
        moduleDisplayLink.setRotation(40);
        moduleDisplayLink.setType(DisplayType.ARROW);
        Mockito.when(moduleLinkFactory.createModuleLink("New Module Link", space)).thenReturn(moduleLink);
        moduleDisplayLink.setLink(moduleLink);

        Mockito.when(moduleLinkDisplayFactory.createModuleLinkDisplay(moduleLink)).thenReturn(moduleDisplayLink);

        Mockito.when(moduleLinkRepo.save((ModuleLink) moduleLink)).thenReturn((ModuleLink)moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save((ModuleLinkDisplay)moduleDisplayLink)).thenReturn((ModuleLinkDisplay)moduleDisplayLink);

        IModuleLinkDisplay savedModuleLinkDisplay1 = managerToTest.createLink("New Module Link", spaceId1, 10, 30, 40, "MOD001", "New Module Link", DisplayType.ARROW, null, null);
        Assert.assertEquals(moduleDisplayLink.getId(), savedModuleLinkDisplay1.getId());
        Assert.assertEquals(moduleDisplayLink.getName(), savedModuleLinkDisplay1.getName());
        Assert.assertEquals(new Double(moduleDisplayLink.getPositionX()), new Double(savedModuleLinkDisplay1.getPositionX()));
        Assert.assertEquals(new Double(moduleDisplayLink.getPositionY()), new Double(savedModuleLinkDisplay1.getPositionY()));
        Assert.assertEquals(moduleDisplayLink.getRotation(), savedModuleLinkDisplay1.getRotation());
        Assert.assertEquals(moduleDisplayLink.getLink().getId(), savedModuleLinkDisplay1.getLink().getId());
        Assert.assertEquals(moduleDisplayLink.getType(), savedModuleLinkDisplay1.getType());
        Assert.assertEquals(moduleDisplayLink.getLink().getTarget().getId(), savedModuleLinkDisplay1.getLink().getTarget().getId());
        Mockito.verify(moduleLinkDisplayRepo).save((ModuleLinkDisplay)moduleDisplayLink);
    }

    @Test
    public void test_updateLink_success() throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {
        ISpace space = new Space();
        space.setId(spaceId1);
        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);

        IModule target = new Module();
        target.setId("MOD001");
        Mockito.when(moduleManager.getModule("MOD001")).thenReturn(target);

        moduleLink.setTarget(target);
        moduleLinkDisplay.setId("MDLD001");
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

        IModuleLinkDisplay actualUpdatedLink = managerToTest.updateLink("TestModuleEdited", spaceId1, 100, 300, 180, "MOD002", "TestModuleEdited", "MOL001", "MDLD001", DisplayType.ALERT, null, null);
        Assert.assertEquals(moduleLinkDisplayUpdated.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(moduleLinkDisplayUpdated.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(moduleLinkDisplayUpdated.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(moduleLinkDisplayUpdated.getLink().getTarget(), actualUpdatedLink.getLink().getTarget());
        Assert.assertEquals(moduleLinkDisplayUpdated.getType(), actualUpdatedLink.getType());
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

        moduleLinkDisplay.setId("MDLD001");
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
