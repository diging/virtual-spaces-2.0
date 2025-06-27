package edu.asu.diging.vspace.core.factory.impl;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

@RunWith(MockitoJUnitRunner.class)
public class SpaceLinkFactoryTest {
    
    @InjectMocks
    private SpaceLinkFactory spaceLinkFactory = new SpaceLinkFactory();
    
    @Mock
    private ISpace sourceSpace;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createSpaceLink_success() {
   
        String title = "Test Space";
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, sourceSpace);
        
        assertEquals(title, link.getName());
        assertEquals(sourceSpace, link.getSpace());
    }
    
    
    
}
