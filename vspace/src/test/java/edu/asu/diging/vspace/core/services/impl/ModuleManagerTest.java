package edu.asu.diging.vspace.core.services.impl;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.Module;

public class ModuleManagerTest {

    @Mock
    private ModuleRepository mockModuleRepo;

    @InjectMocks
    private ModuleManager managerToTest = new ModuleManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getModule_idExists() {

        Module newModule = new Module();
        Optional<Module> mockModule = Optional.of(newModule);
        managerToTest.storeModule(newModule);
        Mockito.when(mockModuleRepo.findById(newModule.getId())).thenReturn(mockModule);

        IModule iModuleActual = managerToTest.getModule(newModule.getId());
        Assert.assertEquals(mockModule.get().getId(), iModuleActual.getId());
        Assert.assertEquals(mockModule.get().getName(), iModuleActual.getName());
    }
    
    @Test
    public void test_getModule_idNotExists() {

        Module newModule = new Module();
        Optional<Module> mockModule = Optional.of(newModule);
        Mockito.when(mockModuleRepo.findById("mockString")).thenReturn(mockModule);

        IModule iModuleActual = managerToTest.getModule("mockString");
        Assert.assertEquals(mockModule.get().getId(), iModuleActual.getId());
        Assert.assertEquals(mockModule.get().getName(), iModuleActual.getName());
    }
}
