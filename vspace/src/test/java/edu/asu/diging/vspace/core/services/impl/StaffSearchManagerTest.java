package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class StaffSearchManagerTest {

    @Mock
    private ISpaceManager spaceManager;

    @Mock
    private IModuleManager moduleManager;

    @Mock
    private ISlideManager slideManager;

    @Mock
    private TextContentBlockRepository textContentBlockRepo;

    @InjectMocks
    private StaffSearchManager serviceToTest;

    private List<Space> spaces;
    
    private List<Space> lastPageSpaces;

    private List<Module> modules;
    
    private List<Module> lastPageModules;

    private List<Slide> slides;
    
    private List<Slide> lastPageSlides;

    private List<Slide> slideTexts;
    
    private List<Slide> lastPageSlideTexts;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        spaces = new ArrayList<>();
        StringBuilder spaceIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            Space space = new Space();
            spaceIdBuilder.setLength(0);
            spaceIdBuilder.append("SPACE_ID_");
            spaceIdBuilder.append(String.valueOf(i));
            space.setId(spaceIdBuilder.toString());
            spaces.add(space);
        }
        
        lastPageSpaces = new ArrayList<>();
        for (int i = 11; i <= 12; i++) {
            Space space = new Space();
            spaceIdBuilder.setLength(0);
            spaceIdBuilder.append("SPACE_ID_");
            spaceIdBuilder.append(String.valueOf(i));
            space.setId(spaceIdBuilder.toString());
            lastPageSpaces.add(space);
        }

        modules = new ArrayList<>();
        StringBuilder moduleIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            Module module = new Module();
            moduleIdBuilder.setLength(0);
            moduleIdBuilder.append("MODULE_ID_");
            moduleIdBuilder.append(String.valueOf(i));
            module.setId(moduleIdBuilder.toString());
            modules.add(module);
        }

        lastPageModules = new ArrayList<>();
        for (int i = 11; i <= 12; i++) {
            Module module = new Module();
            moduleIdBuilder.setLength(0);
            moduleIdBuilder.append("MODULE_ID_");
            moduleIdBuilder.append(String.valueOf(i));
            module.setId(moduleIdBuilder.toString());
            lastPageModules.add(module);
        }
        
        slides = new ArrayList<>();
        StringBuilder slideIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            Slide slide = new Slide();
            slideIdBuilder.setLength(0);
            slideIdBuilder.append("SLIDE_ID_");
            slideIdBuilder.append(String.valueOf(i));
            slide.setId(slideIdBuilder.toString());
            slides.add(slide);
        }

        lastPageSlides = new ArrayList<>();
        for (int i = 11; i <= 12; i++) {
            Slide slide = new Slide();
            slideIdBuilder.setLength(0);
            slideIdBuilder.append("SLIDE_ID_");
            slideIdBuilder.append(String.valueOf(i));
            slide.setId(slideIdBuilder.toString());
            lastPageSlides.add(slide);
        }
        
        slideTexts = new ArrayList<>();
        StringBuilder slideTextIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            Slide slideText = new Slide();
            slideTextIdBuilder.setLength(0);
            slideTextIdBuilder.append("SLIDETEXT_ID_");
            slideTextIdBuilder.append(String.valueOf(i));
            slideText.setId(slideTextIdBuilder.toString());
            slideTexts.add(slideText);
        }
        
        lastPageSlideTexts = new ArrayList<>();
        for (int i = 11; i <= 12; i++) {
            Slide slideText = new Slide();
            slideTextIdBuilder.setLength(0);
            slideTextIdBuilder.append("SLIDETEXT_ID_");
            slideTextIdBuilder.append(String.valueOf(i));
            slideText.setId(slideTextIdBuilder.toString());
            lastPageSlideTexts.add(slideText);
        }

        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);
    }

    @Test
    public void test_searchInSpaces_success() {

        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search))
                .thenReturn(new PageImpl<Space>(spaces,requestedPage, 12));
        Page<Space> tempResult = serviceToTest.searchInSpaces(search, 1);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        StringBuilder dummySpaceIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySpaceIdBuilder.setLength(0);
            dummySpaceIdBuilder.append("SPACE_ID_");
            dummySpaceIdBuilder.append(String.valueOf(i));
            assertEquals(dummySpaceIdBuilder.toString(),idList.get(i-1));
        }
        verify(spaceManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInSpaces_pageGreaterThanTotalPages() {

        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Space>(new ArrayList<Space>(),requestedPage, 12));
        Pageable requestedPage1 = PageRequest.of(1, 10);
        when(spaceManager.findByNameOrDescription(requestedPage1, search)).thenReturn(new PageImpl<Space>(lastPageSpaces,requestedPage1, 12));
        Page<Space> tempResult = serviceToTest.searchInSpaces(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        assertEquals("SPACE_ID_11",idList.get(0));
        assertEquals("SPACE_ID_12",idList.get(1));
        verify(spaceManager).findByNameOrDescription(requestedPage, search);
        verify(spaceManager).findByNameOrDescription(requestedPage1, search);
    }

    @Test
    public void test_searchInSpaces_negativePage() {

        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Space>(spaces, requestedPage, 12));
        Page<Space> tempResult = serviceToTest.searchInSpaces(search, -1);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        StringBuilder dummySpaceIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySpaceIdBuilder.setLength(0);
            dummySpaceIdBuilder.append("SPACE_ID_");
            dummySpaceIdBuilder.append(String.valueOf(i));
            assertEquals(dummySpaceIdBuilder.toString(),idList.get(i-1));
        }
        verify(spaceManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInModules_success() {

        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Module>(modules, requestedPage, 12));
        Page<Module> tempResult = serviceToTest.searchInModules(search, 1);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        StringBuilder dummyModuleIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummyModuleIdBuilder.setLength(0);
            dummyModuleIdBuilder.append("MODULE_ID_");
            dummyModuleIdBuilder.append(String.valueOf(i));
            assertEquals(dummyModuleIdBuilder.toString(),idList.get(i-1));
        }
        verify(moduleManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInModules_pageGreaterThanTotalPages() {

        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Module>(new ArrayList<Module>(), requestedPage, 12));
        Pageable requestedPage1 = PageRequest.of(1, 10);
        when(moduleManager.findByNameOrDescription(requestedPage1, search)).thenReturn(new PageImpl<Module>(lastPageModules, requestedPage1, 12));
        Page<Module> tempResult = serviceToTest.searchInModules(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        assertEquals("MODULE_ID_11",idList.get(0));
        assertEquals("MODULE_ID_12",idList.get(1));
        verify(moduleManager).findByNameOrDescription(requestedPage, search);
        verify(moduleManager).findByNameOrDescription(requestedPage1, search);
    }

    @Test
    public void test_searchInModules_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Module>(modules, requestedPage, 12));
        Page<Module> tempResult = serviceToTest.searchInModules(search, -2);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        StringBuilder dummyModuleIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummyModuleIdBuilder.setLength(0);
            dummyModuleIdBuilder.append("MODULE_ID_");
            dummyModuleIdBuilder.append(String.valueOf(i));
            assertEquals(dummyModuleIdBuilder.toString(),idList.get(i-1));
        }
        verify(moduleManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInSlides_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(slides, requestedPage, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlides(search, 1);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        StringBuilder dummySlideIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySlideIdBuilder.setLength(0);
            dummySlideIdBuilder.append("SLIDE_ID_");
            dummySlideIdBuilder.append(String.valueOf(i));
            assertEquals(dummySlideIdBuilder.toString(),idList.get(i-1));
        }
        verify(slideManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInSlides_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(new ArrayList<Slide>(), requestedPage, 12));
        Pageable requestedPage1 = PageRequest.of(1, 10);
        when(slideManager.findByNameOrDescription(requestedPage1, search)).thenReturn(new PageImpl<Slide>(lastPageSlides, requestedPage1, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlides(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDE_ID_11",idList.get(0));
        assertEquals("SLIDE_ID_12",idList.get(1));
        verify(slideManager).findByNameOrDescription(requestedPage, search);
        verify(slideManager).findByNameOrDescription(requestedPage1, search);
    }

    @Test
    public void test_searchInSlides_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(slides, requestedPage, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlides(search, -2);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        StringBuilder dummySlideIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySlideIdBuilder.setLength(0);
            dummySlideIdBuilder.append("SLIDE_ID_");
            dummySlideIdBuilder.append(String.valueOf(i));
            assertEquals(dummySlideIdBuilder.toString(),idList.get(i-1));
        }
        verify(slideManager).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInSlideTexts_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
                .thenReturn(new PageImpl<Slide>(slideTexts, requestedPage, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, 1);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        StringBuilder dummySlideTextIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySlideTextIdBuilder.setLength(0);
            dummySlideTextIdBuilder.append("SLIDETEXT_ID_");
            dummySlideTextIdBuilder.append(String.valueOf(i));
            assertEquals(dummySlideTextIdBuilder.toString(),idList.get(i-1));
        }
        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchInSlideTexts_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
                .thenReturn(new PageImpl<Slide>(new ArrayList<Slide>(), requestedPage, 12));
        Pageable requestedPage1 = PageRequest.of(1, 10);
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage1, search))
                .thenReturn(new PageImpl<Slide>(lastPageSlideTexts, requestedPage1, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDETEXT_ID_11",idList.get(0));
        assertEquals("SLIDETEXT_ID_12",idList.get(1));
        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage1, search);
    }

    @Test
    public void test_searchInSlideTexts_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
                .thenReturn(new PageImpl<Slide>(slideTexts, requestedPage, 12));
        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, -2);
        assertEquals(10, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        StringBuilder dummySlideTextIdBuilder = new StringBuilder();
        for (int i = 1; i <= 10; i++) {
            dummySlideTextIdBuilder.setLength(0);
            dummySlideTextIdBuilder.append("SLIDETEXT_ID_");
            dummySlideTextIdBuilder.append(String.valueOf(i));
            assertEquals(dummySlideTextIdBuilder.toString(),idList.get(i-1));
        }
        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
    }

}
