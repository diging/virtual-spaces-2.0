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
    private ModuleRepository moduleRepo;

    @InjectMocks
    private ModuleManager managerToTest = new ModuleManager();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_getModule_success() {

        Module newModule = new Module();
        Optional<Module> findModule = Optional.of(newModule);
        managerToTest.storeModule(newModule);
        Mockito.when(moduleRepo.findById(newModule.getId())).thenReturn(findModule);
        IModule moduleTest = managerToTest.getModule(findModule.get().getId());
        Assert.assertEquals(moduleTest, newModule);
    }

}
