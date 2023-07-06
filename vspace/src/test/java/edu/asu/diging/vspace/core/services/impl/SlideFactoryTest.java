package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.factory.impl.SlideFactory;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public class SlideFactoryTest {
    
    @Mock
    private SlideRepository slideRepo;
    
    @Mock
    private ExhibitionManager exhibitionManager;
    
    @InjectMocks
    private SlideFactory serviceToTest;
    
    
    @Test
    public void test_createNewSlideForm_success() {
        Exhibition exhibition = new Exhibition();
        List<IExhibitionLanguage> languageList =  new ArrayList<IExhibitionLanguage>();
        ExhibitionLanguage  language2 = new ExhibitionLanguage();


        language2.setLabel("English");
        languageList.add(language2);
        exhibition.setLanguages(languageList);

        List<Slide> spacePageList = new ArrayList();
        Slide slidePage = new Slide();        
        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");        
        List<ILocalizedText> titleList = new ArrayList<ILocalizedText>();     
        titleList.add(new LocalizedText(language2, "title1"));        
        List<ILocalizedText> slideTextList = new ArrayList<ILocalizedText>();
        slideTextList.add(new LocalizedText( language2, "slide text"));      
        slidePage.setSlideNames(titleList);       
        slidePage.setSlideDescriptions(slideTextList);
        spacePageList.add(slidePage);

        when(slideRepo.findAll()).thenReturn(spacePageList);

        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);      

        SlideForm  slideForm =   serviceToTest.createNewSlideForm(slidePage);
        assertEquals(slideForm.getDescriptions().size(), 1);

        assertEquals(slideForm.getNames().size(), 1);

        assertEquals(slideForm.getDescriptions().get(0).getText(), "slide text");

    }

}