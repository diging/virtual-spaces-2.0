package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;

@RunWith(MockitoJUnitRunner.class)
public class ModuleLinkFactoryTest {
    @InjectMocks
    private ModuleLinkFactory moduleLinkFactory;

    @Mock
    private ISpace space;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void test_createModuleLink_success() {
        String title = "Test Module";
        IModuleLink link = moduleLinkFactory.createModuleLink(title, space);
        
        assertEquals(title, link.getName());
        assertEquals(space, link.getSpace());
    }

}
