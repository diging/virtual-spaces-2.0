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
    private LocalizedTextRepository localizedRextRepo;
    
    @Mock
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

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
    
    @Test
    public void test_storeAboutPageData_failure() {
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


        when(localizedRextRepo.findById("ID1") ).thenReturn(Optional.empty());
        when(localizedRextRepo.findById("ID2") ).thenReturn(Optional.empty());
        when(exhibitionLanguageRepository.findById("langId")).thenReturn(Optional.empty());
        serviceToTest.storeAboutPageData(aboutPageForm);
        assertEquals(locText1.getText(), null);
    } 
    
    @Test
    public void test_createAboutPageForm_success() {
        Exhibition exhibition = new Exhibition();
        List<IExhibitionLanguage> languageList =  new ArrayList<IExhibitionLanguage>();
        ExhibitionLanguage  language2 = new ExhibitionLanguage();
        
   
        language2.setLabel("English");
        languageList.add(language2);
        exhibition.setLanguages(languageList);
               
        List<ExhibitionAboutPage> exhibitionAboutPageList = new ArrayList();
        ExhibitionAboutPage exhbitionAboutPage = new ExhibitionAboutPage();        
        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");        
        List<ILocalizedText> titleList = new ArrayList<ILocalizedText>();     
        titleList.add(new LocalizedText(language2, "title1"));        
        List<ILocalizedText> aboutTextList = new ArrayList<ILocalizedText>();
        aboutTextList.add(new LocalizedText( language2, "about text"));      
        exhbitionAboutPage.setExhibitionTitles(titleList);        
        exhbitionAboutPage.setExhibitionTextDescriptions(aboutTextList);
        exhibitionAboutPageList.add(exhbitionAboutPage);

        when(repo.findAll()).thenReturn(exhibitionAboutPageList);
         
        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);      
        
        AboutPageForm  aboutPageForm =   serviceToTest.createAboutPageForm();
        assertEquals(aboutPageForm.getAboutPageTexts().size(), 1);
        
        assertEquals(aboutPageForm.getTitles().size(), 1);
        
        assertEquals(aboutPageForm.getAboutPageTexts().get(0).getText(), "about text");

    }
    
}
