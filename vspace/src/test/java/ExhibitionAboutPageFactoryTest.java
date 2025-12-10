import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
import edu.asu.diging.vspace.web.staff.forms.factory.AboutPageFormFactory;
import edu.asu.diging.vspace.web.staff.forms.factory.LocalizedTextFormFactory;

public class ExhibitionAboutPageFactoryTest {
    
    @InjectMocks
    private AboutPageFormFactory serviceToTest;
    
    @Mock
    private ExhibitionAboutPageRepository repo;
    
    @Mock
    private IExhibitionManager exhibitionManager;
    
    @Mock
    private LocalizedTextFormFactory localizedTextFormCreation;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
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

        //when(repo.findAll()).thenReturn(exhibitionAboutPageList);         
        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);           
        AboutPageForm  aboutPageForm =   serviceToTest.createAboutPageForm(exhbitionAboutPage);
        
        assertEquals(aboutPageForm.getAboutPageTexts().size(), 1);
        assertEquals(aboutPageForm.getTitles().size(), 1);
    }
}
