package edu.asu.diging.vspace.core.services.impl;

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
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;

public class ModuleLinkManagerTest {
    @Mock
    private ModuleLinkRepository moduleLinkRepo;

    @Mock
    private ModuleLinkDisplayRepository moduleLinkDisplayRepo;

    @InjectMocks
    private ModuleLinkManager managerToTest = new ModuleLinkManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getDisplayLink_idExists() {
        ModuleLinkDisplay newModuleLinkDisplay = new ModuleLinkDisplay();
        newModuleLinkDisplay.setId("MDLD001");
        Optional<ModuleLinkDisplay> mockModuleLinkDisplay = Optional.of(newModuleLinkDisplay);
        Mockito.when(moduleLinkDisplayRepo.findById(newModuleLinkDisplay.getId())).thenReturn(mockModuleLinkDisplay);

        IModuleLinkDisplay moduleLinkDisplayActual = managerToTest.getDisplayLink(newModuleLinkDisplay.getId());
        Assert.assertEquals(mockModuleLinkDisplay.get().getId(), moduleLinkDisplayActual.getId());
    }

    @Test
    public void test_getDisplayLink_idNotExists() throws Exception {
        Mockito.when(moduleLinkDisplayRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getDisplayLink(Mockito.anyString()));
    }

    @Test
    public void test_getLink_idExists() {
        ModuleLink newModuleLink = new ModuleLink();
        newModuleLink.setId("MOL001");
        Optional<ModuleLink> mockModuleLink = Optional.of(newModuleLink);
        Mockito.when(moduleLinkRepo.findById(newModuleLink.getId())).thenReturn(mockModuleLink);

        IModuleLink moduleLinkActual = managerToTest.getLink(newModuleLink.getId());
        Assert.assertEquals(mockModuleLink.get().getId(), moduleLinkActual.getId());
    }

    @Test
    public void test_getLink_idNotExists() throws Exception {
        Mockito.when(moduleLinkRepo.findById(Mockito.anyString())).thenReturn(Optional.empty());
        Assert.assertNull(managerToTest.getLink(Mockito.anyString()));
    }

    @Test
    public void test_deleteLinkDisplayRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);
        moduleLinkDisplay.setName("TestModule");
        moduleLinkDisplay.setPositionX(10);
        moduleLinkDisplay.setPositionY(30);
        moduleLinkDisplay.setRotation(20);
        moduleLinkDisplay.setType(DisplayType.MODULE);
        moduleLinkDisplay.setLink(moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save(moduleLinkDisplay)).thenReturn(moduleLinkDisplay);
        managerToTest.deleteLinkDisplayRepo(moduleLink);
        Mockito.verify(moduleLinkDisplayRepo).deleteByLink(moduleLink);
    }

    @Test
    public void test_deleteLinkRepo_linkPresent(){
        ISpace space = new Space();
        space.setId("SPA001");
        ModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);
        Mockito.when(moduleLinkRepo.save(moduleLink)).thenReturn(moduleLink);
        managerToTest.deleteLinkRepo(moduleLink);
        Mockito.verify(moduleLinkRepo).delete(moduleLink);
    }

    @Test
    public void test_updateDisplayLink_success() {
        ISpace space = new Space();
        space.setId("SPA001");
        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);
        moduleLinkDisplay.setName("TestModule");
        moduleLinkDisplay.setPositionX(10);
        moduleLinkDisplay.setPositionY(30);
        moduleLinkDisplay.setRotation(20);
        moduleLinkDisplay.setType(DisplayType.MODULE);
        moduleLinkDisplay.setLink(moduleLink);
        Mockito.when(moduleLinkDisplayRepo.save(moduleLinkDisplay)).thenReturn(moduleLinkDisplay);

        moduleLinkDisplay.setName("TestModuleEdited");
        moduleLinkDisplay.setPositionX(100);
        moduleLinkDisplay.setPositionY(300);
        moduleLinkDisplay.setRotation(200);

        IModuleLinkDisplay actualUpdatedLink = managerToTest.updateLinkAndDisplay(moduleLink, moduleLinkDisplay);
        Assert.assertEquals(moduleLinkDisplay.getId(), actualUpdatedLink.getId());
        Assert.assertEquals(moduleLinkDisplay.getName(), actualUpdatedLink.getName());
        Assert.assertEquals(new Double(moduleLinkDisplay.getPositionX()), new Double(actualUpdatedLink.getPositionX()));
        Assert.assertEquals(new Double(moduleLinkDisplay.getPositionY()), new Double(actualUpdatedLink.getPositionY()));
        Assert.assertEquals(moduleLinkDisplay.getRotation(), actualUpdatedLink.getRotation());
        Assert.assertEquals(moduleLinkDisplay.getLink().getId(), actualUpdatedLink.getLink().getId());
        Assert.assertEquals(moduleLinkDisplay.getType(), actualUpdatedLink.getType());
    }
}
