package edu.asu.diging.vspace.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitionLanguageConfigTest {

    @InjectMocks
    private ExhibitionLanguageConfig exhibitionLanguageConfig;

    @Mock
    private AbstractEnvironment environment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        exhibitionLanguageConfig = new ExhibitionLanguageConfig();
        ReflectionTestUtils.setField(exhibitionLanguageConfig, "environment", environment);
    }

    @Test
    public void testInit() {
        // Set up test data
        List<Map> expectedExhibitionLanguageList = new ArrayList<Map>();
        Map<String, Object> languageMap = new HashMap<String, Object>();
        languageMap.put(ConfigConstants.LANGUAGES, expectedExhibitionLanguageList);
        MapPropertySource propertySource = new MapPropertySource(ConfigConstants.EXHIBITION_LANGUAGE_LIST_PROPERTY,
                languageMap);

        // Set up mock environment
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.addFirst(propertySource);
        when(environment.getPropertySources()).thenReturn(propertySources);

        exhibitionLanguageConfig.init();

        List<Map> actualExhibitionLanguageList = (List<Map>) ReflectionTestUtils.getField(exhibitionLanguageConfig,
                "exhibitionLanguageList");
        assertSame(expectedExhibitionLanguageList, actualExhibitionLanguageList);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithNullPropertySource() {
        // Define the mock behavior for the environment

        when(environment.getPropertySources()).thenReturn(null);
        // Call the init method on the ExhibitionLanguageConfig object
        exhibitionLanguageConfig.init();

        // Verify that the exhibitionLanguageList property is empty
        assertNotNull(exhibitionLanguageConfig.getExhibitionLanguageList());
        assertTrue(exhibitionLanguageConfig.getExhibitionLanguageList().isEmpty());
    }

    @Test
    public void testSetterAndGetter() {
        List<Map> languageList = Arrays.asList(Map.of("key1", "value1"), Map.of("key2", "value2"));

        exhibitionLanguageConfig.setExhibitionLanguageList(languageList);

        assertNotNull(exhibitionLanguageConfig.getExhibitionLanguageList());
        assertEquals(languageList, exhibitionLanguageConfig.getExhibitionLanguageList());
    }

}
