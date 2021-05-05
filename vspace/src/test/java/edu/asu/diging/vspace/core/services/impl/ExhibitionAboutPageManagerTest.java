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

/**
 * 
 * @author Avirup Biswas
 *
 */
public class ExhibitionAboutPageManagerTest {

    @Mock
    private ExhibitionAboutPageRepository repo;

    @InjectMocks
    private ExhibitionAboutPageManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_storeExhibitionAbtPage_success() {
        ExhibitionAboutPage aboutPage = new ExhibitionAboutPage();
        aboutPage.setId("EXHABT000000001");
        when(repo.save(aboutPage)).thenReturn(aboutPage);
        ExhibitionAboutPage savedExhibAbtPage = serviceToTest.store(aboutPage);
        assertNotNull(savedExhibAbtPage.getId());
        verify(repo).save(aboutPage);
    }

    @Test
    public void test_findAll_findMany() {
        ExhibitionAboutPage aboutPage1 = new ExhibitionAboutPage();
        ExhibitionAboutPage aboutPage2 = new ExhibitionAboutPage();
        ExhibitionAboutPage aboutPage3 = new ExhibitionAboutPage();
        ExhibitionAboutPage aboutPage4 = new ExhibitionAboutPage();
        ExhibitionAboutPage aboutPage5 = new ExhibitionAboutPage();
        List<ExhibitionAboutPage> exhibitionAbtPageList = new ArrayList<ExhibitionAboutPage>();
        exhibitionAbtPageList.add(aboutPage1);
        exhibitionAbtPageList.add(aboutPage2);
        exhibitionAbtPageList.add(aboutPage3);
        exhibitionAbtPageList.add(aboutPage4);
        exhibitionAbtPageList.add(aboutPage5);
        when(repo.findAll()).thenReturn(exhibitionAbtPageList);
        List<ExhibitionAboutPage> results = serviceToTest.findAll();
        assertEquals(results.size(), 5);
        verify(repo).findAll();
    }

    @Test
    public void test_findAll_findNone() {
        List<ExhibitionAboutPage> exhibitionAbtPageList = new ArrayList<ExhibitionAboutPage>();
        when(repo.findAll()).thenReturn(exhibitionAbtPageList);
        List<ExhibitionAboutPage> results = serviceToTest.findAll();
        assertEquals(results.size(), 0);
        verify(repo).findAll();
    }
}
