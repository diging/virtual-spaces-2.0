package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
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

    @Spy
    @InjectMocks
    private StaffSearchManager serviceToTest;

    private List<IVSpaceElement> spaces;
    
    private List<IVSpaceElement> modules;
    
    private List<IVSpaceElement> slides;
    
    private List<IVSpaceElement> slideTexts;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Space space1 = new Space();
        space1.setId("SPACE_ID_1");
        Space space2 = new Space();
        space2.setId("SPACE_ID_2");
        spaces = new ArrayList<>();
        spaces.add(space1);
        spaces.add(space2);
        Module module1 = new Module();
        module1.setId("MODULE_ID_1");
        Module module2 = new Module();
        module2.setId("MODULE_ID_2");
        modules = new ArrayList<>();
        modules.add(module1);
        modules.add(module2);
        Slide slide1 = new Slide();
        slide1.setId("SLIDE_ID_1");
        Slide slide2 = new Slide();
        slide2.setId("SLIDE_ID_2");
        slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);
        Slide slideText1 = new Slide();
        slideText1.setId("SLIDETEXT_ID_1");
        Slide slideText2 = new Slide();
        slideText2.setId("SLIDETEXT_ID_2");
        slideTexts = new ArrayList<>();
        slideTexts.add(slideText1);
        slideTexts.add(slideText2);
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);
    }

    @Test
    public void test_searchSpaces_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSpaces(search,1);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager,times(2)).findByNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchSpaces_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        Mockito.doReturn(6L).when(serviceToTest).getTotalSpacePages(search);
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSpaces(search,7);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSpaces_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSpaces(search,-1);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager,times(2)).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchModules_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInModules(search,1);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager,times(2)).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchModules_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        Mockito.doReturn(6L).when(serviceToTest).getTotalModulePages(search);
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInModules(search,7);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchModules_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInModules(search,-2);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager,times(2)).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlides_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlides(search,1);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager,times(2)).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlides_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        Mockito.doReturn(6L).when(serviceToTest).getTotalSlidePages(search);
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlides(search,7);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlides_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlides(search,-2);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager,times(2)).findByNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlideTexts_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlideTexts(search,1);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo,times(2)).findWithNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlideTexts_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        Mockito.doReturn(6L).when(serviceToTest).getTotalSlideTextPages(search);
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlideTexts(search,7);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlideTexts_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchInSlideTexts(search,-2);
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo,times(2)).findWithNameOrDescription(requestedPage, search);
    }
}
