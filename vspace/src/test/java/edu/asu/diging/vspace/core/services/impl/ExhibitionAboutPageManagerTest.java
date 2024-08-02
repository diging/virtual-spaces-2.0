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
import org.mockito.Spy;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
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
    private LocalizedTextRepository localizedTextRepo;
    
    @Mock
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_store_success() {
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
        when(localizedTextRepo.findById("ID1") ).thenReturn(Optional.of(locText1));
        when(localizedTextRepo.findById("ID2") ).thenReturn(Optional.of(locText2));


        ExhibitionAboutPage aboutPage = new ExhibitionAboutPage();
        Exhibition exhibition = new Exhibition();
        when(exhibitionManager.getStartExhibition()).thenReturn((IExhibition)exhibition);
        when(repo.save(aboutPage)).thenReturn(aboutPage);
        ExhibitionAboutPage savedExhibAbtPage = serviceToTest.store(aboutPageForm);
        assertEquals(locText1.getText(), "title");
        assertEquals(locText2.getText(), "about text");
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
    public void test_store_failure() {

        Exhibition exhibition = new Exhibition();
        List<ExhibitionAboutPage> exhibitionAboutPageList = new ArrayList();

        exhibitionAboutPageList.add(new ExhibitionAboutPage());

        AboutPageForm aboutPageForm = new AboutPageForm();
        List<LocalizedTextForm> titleList = new ArrayList<LocalizedTextForm>();
        titleList.add(new LocalizedTextForm("title", "ID1", "langId", "English"));
        List<LocalizedTextForm> aboutTextList = new ArrayList<LocalizedTextForm>();

        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");
        aboutTextList.add(new LocalizedTextForm( "about text","ID2", "langId", "English"));
        aboutPageForm.setTitles(titleList);
        aboutPageForm.setAboutPageTexts(aboutTextList);
        
        when(exhibitionManager.getStartExhibition()).thenReturn((IExhibition)exhibition);
        when(localizedTextRepo.findById("ID1") ).thenReturn(Optional.empty());
        when(localizedTextRepo.findById("ID2") ).thenReturn(Optional.empty());
        when(exhibitionLanguageRepository.findById("langId")).thenReturn(Optional.empty());
        when(repo.findAll()).thenReturn(exhibitionAboutPageList);
        
        serviceToTest.store(aboutPageForm);
        assertEquals(locText1.getText(), null);
    } 
   
    
}
