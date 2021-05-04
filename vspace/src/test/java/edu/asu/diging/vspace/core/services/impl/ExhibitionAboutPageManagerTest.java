package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;

public class ExhibitionAboutPageManagerTest {

    @Mock
    private ExhibitionAboutPageRepository exhAbtRepo;
    
    @InjectMocks
    private ExhibitionAboutPageManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_storeExhibitionAbtPage_success() {
        ExhibitionAboutPage exhibitionAbtPage = new ExhibitionAboutPage();
        when(exhAbtRepo.save(exhibitionAbtPage)).thenReturn(exhibitionAbtPage);
        ExhibitionAboutPage savedExhibAbtPage = serviceToTest.storeExhibitionAbtPage(exhibitionAbtPage);
        assertNotNull(savedExhibAbtPage);
        verify(exhAbtRepo).save(exhibitionAbtPage);
    }
    
    @Test
    public void test_findAll_success() {
        ExhibitionAboutPage exhibitionAbtPage = new ExhibitionAboutPage();
        List<ExhibitionAboutPage> exhibitionAbtPageList = new ArrayList<ExhibitionAboutPage>();
        exhibitionAbtPageList.add(exhibitionAbtPage);
        when(exhAbtRepo.findAll()).thenReturn(exhibitionAbtPageList);
        List<ExhibitionAboutPage> results =  serviceToTest.findAll();
        assertEquals(results.size(),1);
        verify(exhAbtRepo).findAll();
    }
}
