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
    @Mock
    private AbstractEnvironment environment;

    @InjectMocks
    private ExhibitionLanguageConfig serviceToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serviceToTest = new ExhibitionLanguageConfig();
        ReflectionTestUtils.setField(serviceToTest, "environment", environment);
    }

    @Test
    public void test_init_success() {
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

        serviceToTest.init();

        List<Map> actualExhibitionLanguageList = (List<Map>) ReflectionTestUtils.getField(serviceToTest,
                "exhibitionLanguageList");
        assertSame(expectedExhibitionLanguageList, actualExhibitionLanguageList);
    }

    @Test(expected = NullPointerException.class)
    public void test_init_withNullPropertySource() {
        // Define the mock behavior for the environment

        when(environment.getPropertySources()).thenReturn(null);
        // Call the init method on the ExhibitionLanguageConfig object
        serviceToTest.init();

        // Verify that the exhibitionLanguageList property is empty
        assertNotNull(serviceToTest.getExhibitionLanguageList());
        assertTrue(serviceToTest.getExhibitionLanguageList().isEmpty());
    }

    @Test
    public void test_getExhibitionLanguageList_success() {
        Map<String, String> map1 = new HashMap();
        map1.put("key1", "value1");
        
        Map<String, String> map2 = new HashMap();
        map2.put("key2", "value2");
        List<Map> languageList = Arrays.asList(map1, map2);

        serviceToTest.setExhibitionLanguageList(languageList);

        assertNotNull(serviceToTest.getExhibitionLanguageList());
        assertEquals(languageList, serviceToTest.getExhibitionLanguageList());
    }

}
