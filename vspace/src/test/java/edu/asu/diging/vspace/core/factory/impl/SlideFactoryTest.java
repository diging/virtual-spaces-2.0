package edu.asu.diging.vspace.core.factory.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SlideFactoryTest {
    
    @InjectMocks
    private SlideFactory slideFactory;
    
    @Mock
    private IModuleManager moduleManager;
    
    private String moduleId;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        moduleId = "MOD000000002";
    }

    @Test
    public void test_createSlide_success() {
        SlideForm form = mock(SlideForm.class);
        String name = "Test Slide";
        String description = "This is a test Slide";
        when(form.getName()).thenReturn(name);
        when(form.getDescription()).thenReturn(description);
        when(form.getType()).thenReturn("SLIDE");
        
        IModule module = moduleManager.getModule(moduleId);
        SlideType type = form.getType().isEmpty() ? null : SlideType.valueOf(form.getType());
        
        ISlide slide = slideFactory.createSlide(module, form, type);
        assertEquals(name, slide.getName());
        assertEquals(description, slide.getDescription());
    }

}