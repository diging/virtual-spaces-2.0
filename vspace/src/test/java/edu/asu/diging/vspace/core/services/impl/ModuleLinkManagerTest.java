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
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;

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

        ModuleLinkDisplay moduleLinkDisplayActual = managerToTest.getDisplayLink(newModuleLinkDisplay.getId());
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
}
