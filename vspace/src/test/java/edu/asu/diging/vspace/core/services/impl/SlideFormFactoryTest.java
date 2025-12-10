package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.ModuleFactory;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;
import edu.asu.diging.vspace.web.staff.forms.factory.LocalizedTextFormFactory;
import edu.asu.diging.vspace.web.staff.forms.factory.SlideFormFactory;

public class SlideFormFactoryTest {
    
    @Mock
    private SlideRepository slideRepo;
    
    @Mock
    private ExhibitionManager exhibitionManager;
    
    @InjectMocks
    private SlideFormFactory serviceToTest;
    
    @Mock
    private ModuleFactory moduleFactory;
    
    @Mock
    private LocalizedTextFormFactory localizedTextFormCreation;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize annotated mocks
    }

    @Test
    public void test_createNewSlideForm_success() {
        Exhibition exhibition = new Exhibition();
        List<IExhibitionLanguage> languageList =  new ArrayList<IExhibitionLanguage>();
        ExhibitionLanguage  language2 = new ExhibitionLanguage();

        language2.setLabel("English");
        languageList.add(language2);
        exhibition.setLanguages(languageList);

        List<Slide> slidePageList = new ArrayList<>();
        Slide slidePage = new Slide();        
        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");        
        List<ILocalizedText> titleList = new ArrayList<ILocalizedText>();     
        titleList.add(new LocalizedText(language2, "title1"));        
        List<ILocalizedText> slideTextList = new ArrayList<ILocalizedText>();
        slideTextList.add(new LocalizedText( language2, "slide text"));      
        slidePage.setSlideNames(titleList);       
        slidePage.setSlideDescriptions(slideTextList);
        slidePageList.add(slidePage);

        when(slideRepo.findAll()).thenReturn(slidePageList);
        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);           
        SlideForm slideForm =   serviceToTest.createNewSlideForm(slidePage, exhibition);

        assertEquals(slideForm.getDescriptions().size(), 1);
        assertEquals(slideForm.getNames().size(), 1);
    }

}