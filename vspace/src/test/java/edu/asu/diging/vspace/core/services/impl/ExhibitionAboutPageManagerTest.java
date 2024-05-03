package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;

/**
 * 
 * @author Avirup Biswas
 *
 */
public class ExhibitionAboutPageManagerTest {
    
    @Mock
    private ArrayList<ExhibitionAboutPage> mockArrayList;
        
    @Mock
    private ExhibitionAboutPageRepository repo;
    
    @Mock
    private ExhibitionManager exhibitionManager;

    @InjectMocks
    private ExhibitionAboutPageManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_store_success() {
        ExhibitionAboutPage aboutPage = new ExhibitionAboutPage();
        Exhibition exhibition = new Exhibition();
        aboutPage.setId("EXHABT000000001");
        when(exhibitionManager.getStartExhibition()).thenReturn((IExhibition)exhibition);
        when(repo.save(aboutPage)).thenReturn(aboutPage);
        ExhibitionAboutPage savedExhibAbtPage = serviceToTest.store(aboutPage);
        assertNotNull(savedExhibAbtPage.getId());
        verify(repo).save(aboutPage);
    }

    @Test
    public void test_findAll_findMany() {   
        when(repo.findAll()).thenReturn(Arrays.asList(Mockito.mock(ExhibitionAboutPage.class), Mockito.mock(ExhibitionAboutPage.class), Mockito.mock(ExhibitionAboutPage.class)));
        List<ExhibitionAboutPage> results = serviceToTest.findAll();
        assertEquals(results.size(), 3);
    }

    @Test
    public void test_findAll_findNone() {
        when(repo.findAll()).thenReturn(mockArrayList);
        List<ExhibitionAboutPage> results = serviceToTest.findAll();
        assertEquals(results.size(), 0);
    }
    
}
