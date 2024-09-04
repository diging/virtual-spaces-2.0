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

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.SpaceFactory;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;
import edu.asu.diging.vspace.web.staff.forms.factory.LocalizedTextFormFactory;
import edu.asu.diging.vspace.web.staff.forms.factory.SpaceFormFactory;

public class SpaceFactoryTest {
    
    @Mock
    private SpaceRepository spaceRepo;
    
    @Mock
    private ExhibitionManager exhibitionManager;
    
    @InjectMocks
    private SpaceFormFactory serviceToTest;
    
    @Mock
    private LocalizedTextFormFactory localizedTextFormCreation;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize annotated mocks
    }
   
    @Test
    public void test_createNewSpaceForm_success() {
        Exhibition exhibition = new Exhibition();
        List<IExhibitionLanguage> languageList =  new ArrayList<IExhibitionLanguage>();
        ExhibitionLanguage  language2 = new ExhibitionLanguage();
        language2.setLabel("English");
        languageList.add(language2);
        exhibition.setLanguages(languageList);
        List<Space> spacePageList = new ArrayList<Space>();
        Space spacePage = new Space();     
        LocalizedText locText1 =  new LocalizedText();
        locText1.setId( "ID1");        
        List<ILocalizedText> titleList = new ArrayList<ILocalizedText>();     
        titleList.add(new LocalizedText(language2, "title1"));        
        List<ILocalizedText> spaceTextList = new ArrayList<ILocalizedText>();
        spaceTextList.add(new LocalizedText( language2, "space text"));      
        spacePage.setSpaceNames(titleList);       
        spacePage.setSpaceDescriptions(spaceTextList);
        spacePageList.add(spacePage);
              
        when(spaceRepo.findAll()).thenReturn(spacePageList);
        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);      
        SpaceForm spaceForm = serviceToTest.createNewSpaceForm(spacePage, exhibitionManager.getStartExhibition());
        
        assertEquals(spaceForm.getDescriptions().size(), 1);
        assertEquals(spaceForm.getNames().size(), 1);
    }

}