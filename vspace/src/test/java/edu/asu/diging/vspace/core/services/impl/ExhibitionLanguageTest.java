package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;

public class ExhibitionLanguageTest {


    @InjectMocks
    ExhibitionManager exhibitionManager;

    @Mock
    private ExhibitionRepository exhibitRepo;

    @Mock
    private ExhibitionLanguageConfig exhibitionLanguageConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test_updateExhibitionLanguages_success(){
        Exhibition exhibition = new Exhibition();

        List<String> languages= new ArrayList() ;

        languages.add("en");
        languages.add("aa");

        List<Map> mappedLanguages= new ArrayList();

        Map<String, String> language1 =    new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 =   new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        exhibitionManager.updateExhibitionLanguages(exhibition, languages,null);
        assertEquals(exhibition.getLanguages().size(),2);

    }

    @Test
    public void test_updateExhibitionLanguages_duplicates(){
        Exhibition exhibition = new Exhibition();

        //Exhibition already consists of 2 languages
        exhibition.getLanguages().add(new ExhibitionLanguage("English", "en", exhibition));
        exhibition.getLanguages().add(new ExhibitionLanguage("Afar", "aa", exhibition));


        List<String> languages= new ArrayList() ;

        languages.add("en");
        languages.add("aa");
        languages.add("sq"); // new language added

        List<Map> mappedLanguages= new ArrayList();

        Map<String, String> language1 =    new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 =   new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        Map<String, String> language3 =   new LinkedHashMap<String, String>();
        language3.put("code", "sq");
        language3.put("label", "Albanian");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        mappedLanguages.add(language3);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        exhibitionManager.updateExhibitionLanguages(exhibition, languages, null);

        //no duplicate entries should be added
        assertEquals(exhibition.getLanguages().size(),3);
    }

    @Test
    public void test_updateExhibitionLanguages_whenCodeIsNotPresentInConfig(){
        Exhibition exhibition = new Exhibition();

        List<String> languages= new ArrayList() ;

        languages.add("en");
        languages.add("Invalid");
        List<Map> mappedLanguages= new ArrayList();

        Map<String, String> language1 =    new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 =   new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        exhibitionManager.updateExhibitionLanguages(exhibition, languages,null);
        assertEquals(exhibition.getLanguages().size(),1);


    }

    @Test
    public void test_updateExhibitionLanguages_defaultLanguage(){
        Exhibition exhibition = new Exhibition();

        List<String> languages= new ArrayList() ;
        languages.add("en");
        languages.add("aa");  
        List<Map> mappedLanguages= new ArrayList();

        Map<String, String> language1 =    new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 =   new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        exhibitionManager.updateExhibitionLanguages(exhibition, languages,"en");
        assertEquals(exhibition.getLanguages().size(),2);
        exhibition.getLanguages().forEach(language -> {
            if(language.getCode().equals("en")) { 
                assertTrue(language.isDefault());
      
            } });

    }
}
