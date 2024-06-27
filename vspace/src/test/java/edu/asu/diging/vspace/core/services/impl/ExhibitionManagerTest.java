package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.config.ExhibitionLanguageConfig;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.LanguageListConfigurationNotFoundException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.services.IImageService;


@RunWith(MockitoJUnitRunner.class)
public class ExhibitionManagerTest {

    @Mock
    private ExhibitionRepository exhibitRepo;

    @Mock
    private IStorageEngine storage;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private IImageService imageService;

    @Mock
    private ImageRepository imageRepo;
    
    @Mock
    private ExhibitionLanguageConfig exhibitionLanguageConfig;
    
    @Mock
    private ExhibitionFactory exhibitFactory;


    @InjectMocks
    private ExhibitionManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    private final String ID = "IMAGE_ID";

    @Test
    public void test_storeExhibition_success() {
        Exhibition exhibition = new Exhibition();
        when(exhibitRepo.save(exhibition)).thenReturn(exhibition);
        IExhibition exhibitionTest = serviceToTest.storeExhibition(exhibition);
        assertNotNull(exhibitionTest);
        verify(exhibitRepo).save(exhibition);
    }

    @Test
    public void test_getExhibitionById_success() {
        
        Exhibition exhibition = new Exhibition();
        exhibition.setId(ID);
        Optional<Exhibition> exhibitionOptional = Optional.of(exhibition);
        when(exhibitRepo.findById(ID)).thenReturn(exhibitionOptional);
        IExhibition exhibitionTest = serviceToTest.getExhibitionById(ID);
        assertEquals(exhibitionTest, exhibition);
        verify(exhibitRepo).findById(ID);
    }
    
    @Test
    public void test_findAll_success() {
        
        Exhibition exhibition = new Exhibition();
        exhibition.setId(ID);
        List<Exhibition> list = new ArrayList();
        list.add(exhibition);
        
        when(exhibitRepo.findAll()).thenReturn(list);
        List<IExhibition> exhibitionTest = serviceToTest.findAll();
        
        assertEquals(exhibitionTest.size(), list.size());
        assertEquals(exhibitionTest.indexOf(exhibition), 0);
        verify(exhibitRepo).findAll();
    }
    
    
    @Test
    public void test_getStartExhibition_success() {
        
        Exhibition exhibition = new Exhibition();
        exhibition.setId(ID);
        List<Exhibition> list = new ArrayList();
        list.add(exhibition);
        Mockito.when(exhibitRepo.findAllByOrderByIdAsc()).thenReturn(list);
        IExhibition exhibitionTest = serviceToTest.getStartExhibition();
        assertEquals(exhibitionTest, exhibition);
        verify(exhibitRepo).findAllByOrderByIdAsc();
    }

    @Test
    public void test_updateExhibitionLanguages_success() {
        Exhibition exhibition = new Exhibition();

        List<String> languages = new ArrayList();

        languages.add("en");
        languages.add("aa");

        List<Map> mappedLanguages = new ArrayList();

        Map<String, String> language1 = new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 = new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        serviceToTest.updateExhibitionLanguages(exhibition, languages, null);
        assertEquals(exhibition.getLanguages().size(), 2);

    }

    @Test
    public void test_updateExhibitionLanguages_duplicates() {
        Exhibition exhibition = new Exhibition();

        // Exhibition already consists of 2 languages
        exhibition.getLanguages().add(new ExhibitionLanguage("English", "en", exhibition));
        exhibition.getLanguages().add(new ExhibitionLanguage("Afar", "aa", exhibition));

        List<String> languages = new ArrayList();

        languages.add("en");
        languages.add("aa");
        languages.add("sq"); // new language added

        List<Map> mappedLanguages = new ArrayList();

        Map<String, String> language1 = new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 = new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        Map<String, String> language3 = new LinkedHashMap<String, String>();
        language3.put("code", "sq");
        language3.put("label", "Albanian");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        mappedLanguages.add(language3);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        serviceToTest.updateExhibitionLanguages(exhibition, languages, null);

        // no duplicate entries should be added
        assertEquals(exhibition.getLanguages().size(), 3);
    }

    @Test
    public void test_updateExhibitionLanguages_whenCodeIsNotPresentInConfig() {
        Exhibition exhibition = new Exhibition();

        List<String> languages = new ArrayList();

        languages.add("en");
        languages.add("Invalid");
        List<Map> mappedLanguages = new ArrayList();

        Map<String, String> language1 = new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 = new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        serviceToTest.updateExhibitionLanguages(exhibition, languages, null);
        assertEquals(exhibition.getLanguages().size(), 1);

    }

    @Test
    public void test_updateExhibitionLanguages_defaultLanguage() {
        Exhibition exhibition = new Exhibition();

        List<String> languages = new ArrayList();
        languages.add("en");
        languages.add("aa");
        List<Map> mappedLanguages = new ArrayList();

        Map<String, String> language1 = new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 = new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);
        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);
        serviceToTest.updateExhibitionLanguages(exhibition, languages, "en");
        assertEquals(exhibition.getLanguages().size(), 2);
        exhibition.getLanguages().forEach(language -> {
            if (language.getCode().equals("en")) {
                assertTrue(language.isDefault());
            }
        });

        serviceToTest.updateExhibitionLanguages(exhibition, languages, "aa");
        assertEquals(exhibition.getLanguages().size(), 2);
        exhibition.getLanguages().forEach(language -> {
            if (language.getCode().equals("en")) {
                assertFalse(language.isDefault());
            }
            if (language.getCode().equals("aa")) {
                assertTrue(language.isDefault());
            }
        });

    }

    @Test
    public void test_updateExhibitionLanguages_whenLanguageListConfigurationNotFound() {
        Exhibition exhibition = new Exhibition();

        List<String> languages = new ArrayList();

        languages.add("en");

        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(new ArrayList());
        Assert.assertThrows(LanguageListConfigurationNotFoundException.class,
                () -> serviceToTest.updateExhibitionLanguages(exhibition, languages, null));

    }

    @Test
    public void test_updateExhibitionLanguages_whenLanguageIsUnselected() {
        Exhibition exhibition = new Exhibition();

        List<Map> mappedLanguages = new ArrayList();

        Map<String, String> language1 = new LinkedHashMap<String, String>();
        language1.put("code", "en");
        language1.put("label", "English");
        Map<String, String> language2 = new LinkedHashMap<String, String>();
        language2.put("code", "aa");
        language2.put("label", "Afar");
        mappedLanguages.add(language1);
        mappedLanguages.add(language2);

        List<String> languages = new ArrayList();
        languages.add("en");
        languages.add("aa");

        when(exhibitionLanguageConfig.getExhibitionLanguageList()).thenReturn(mappedLanguages);

        serviceToTest.updateExhibitionLanguages(exhibition, languages, "aa");
        assertEquals(exhibition.getLanguages().size(), 2);

        languages.remove("en");
        serviceToTest.updateExhibitionLanguages(exhibition, languages, "aa");
        assertEquals(exhibition.getLanguages().size(), 1);

    }
}
