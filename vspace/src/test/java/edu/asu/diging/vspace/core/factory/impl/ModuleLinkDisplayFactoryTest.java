package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;

@RunWith(MockitoJUnitRunner.class)
public class ModuleLinkDisplayFactoryTest {
    @InjectMocks
    private ModuleLinkDisplayFactory moduleLinkDisplayFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createModuleLinkDisplay_success() {
        IModuleLink link = mock(IModuleLink.class);
        IModuleLinkDisplay display = moduleLinkDisplayFactory.createModuleLinkDisplay(link);
        assertEquals(link, display.getLink());
    }

}
