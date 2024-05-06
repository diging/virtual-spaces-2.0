package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

@RunWith(MockitoJUnitRunner.class)
public class ModuleFactoryTest {

    @InjectMocks
    private ModuleFactory moduleFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createModule_success() {
        ModuleForm form = mock(ModuleForm.class);
        String name = "Test Module";
        String description = "This is a test module";
        when(form.getName()).thenReturn(name);
        when(form.getDescription()).thenReturn(description);
        IModule module = moduleFactory.createModule(form);
        assertEquals(name, module.getName());
        assertEquals(description, module.getDescription());
    }

}
