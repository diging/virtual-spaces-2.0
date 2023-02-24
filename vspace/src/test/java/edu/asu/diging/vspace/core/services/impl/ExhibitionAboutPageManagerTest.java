package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

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
    
    @Mock
    private LocalizedTextRepository localizedRextRepo;

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
    
    @Test
    public void test_storeAboutPageData_success() {
        List<ExhibitionAboutPage> exhibitionAboutPageList = new ArrayList();

        exhibitionAboutPageList.add(new ExhibitionAboutPage());

        AboutPageForm aboutPageForm = new AboutPageForm();
        List<LocalizedTextForm> titleList = new ArrayList<LocalizedTextForm>();
        titleList.add(new LocalizedTextForm("title", "ID1", "langId", "English"));
        List<LocalizedTextForm> aboutTextList = new ArrayList<LocalizedTextForm>();

        aboutTextList.add(new LocalizedTextForm( "about text","ID2", "langId", "English"));


        aboutPageForm.setTitles(titleList);
        aboutPageForm.setAboutPageTexts(aboutTextList);
        when(repo.findAll()).thenReturn(exhibitionAboutPageList);

        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");

        LocalizedText locText2 =  new LocalizedText();
        locText1.setId( "ID2");
        when(localizedRextRepo.findById("ID1") ).thenReturn(Optional.of(locText1));
        when(localizedRextRepo.findById("ID2") ).thenReturn(Optional.of(locText2));

        serviceToTest.storeAboutPageData(aboutPageForm);
        assertEquals(locText1.getText(), "title");
        assertEquals(locText2.getText(), "about text");



    }
    
}
