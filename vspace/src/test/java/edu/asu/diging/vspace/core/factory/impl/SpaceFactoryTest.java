package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@RunWith(MockitoJUnitRunner.class)
public class SpaceFactoryTest {
    
    @InjectMocks
    private SpaceFactory spaceFactory;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpace_success() {
        SpaceForm form = mock(SpaceForm.class);
        String name = "Test Space";
        String description = "This is a test Space";
        when(form.getName()).thenReturn(name);
        when(form.getDescription()).thenReturn(description);
        
        ISpace space = spaceFactory.createSpace(form);
        assertEquals(name, space.getName());
        assertEquals(description, space.getDescription());
    }

}
