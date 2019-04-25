package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.exception.SpaceLinkDoesNotExistException;

public class LinkManagerTest {
    
    @Mock
    private SpaceLinkRepository spaceLinkRepo;
    
    @InjectMocks
    private LinkManager managerToTest;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected = SpaceLinkDoesNotExistException.class)
    public void test_deleteSpaceLinkBySource_whenIdNull() throws SpaceLinkDoesNotExistException {
       Mockito.doThrow(IllegalArgumentException.class)
        .when(spaceLinkRepo).deleteBySourceSpaceId(null);
       managerToTest.deleteSpaceLinkBySource(null);
    }
    
    @Test(expected = SpaceLinkDoesNotExistException.class)
    public void test_deleteSpaceLinkBySource_forNonExistentId() throws SpaceLinkDoesNotExistException {
       String sourceId = "sourceId";
       Mockito.doThrow(EmptyResultDataAccessException.class)
        .when(spaceLinkRepo).deleteBySourceSpaceId(sourceId);
       managerToTest.deleteSpaceLinkBySource(sourceId);
    }
    
}
