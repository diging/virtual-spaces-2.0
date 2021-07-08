package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        String tab = "space";
        when(spaceManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSpaces(search,1,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findInNameOrDescription(requestedPage, search);
    }

    @Test
    public void test_searchSpaces_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "space";
        String tab = "space";
        when(spaceManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSpaces(search,7,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSpaces_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        String tab = "space";
        when(spaceManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(spaces));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSpaces(search,-1,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_getCountOfSearchedSpace_success() {
        String search = "space";
        when(spaceManager.findInNameOrDescription(search)).thenReturn(6L);
        assertEquals(6L, serviceToTest.getCountOfSearchedSpace(search));
    }
    
    @Test
    public void test_searchModules_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        String tab = "module";
        when(moduleManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchModules(search,1,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchModules_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "module";
        String tab = "module";
        when(moduleManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchModules(search,7,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchModules_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        String tab = "module";
        when(moduleManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(modules));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchModules(search,-2,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_getCountOfSearchedModule_success() {
        String search = "module";
        when(moduleManager.findInNameOrDescription(search)).thenReturn(6L);
        assertEquals(6L, serviceToTest.getCountOfSearchedModule(search));
    }
    
    @Test
    public void test_searchSlides_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        String tab = "slide";
        when(slideManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlides(search,1,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlides_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "slide";
        String tab = "slide";
        when(slideManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlides(search,7,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlides_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        String tab = "slide";
        when(slideManager.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slides));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlides(search,-2,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_getCountOfSearchedSlide_success() {
        String search = "slide";
        when(slideManager.findInNameOrDescription(search)).thenReturn(6L);
        assertEquals(6L, serviceToTest.getCountOfSearchedSlide(search));
    }
    
    @Test
    public void test_searchSlideTexts_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        String tab = "slideText";
        when(textContentBlockRepo.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlideTexts(search,1,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlideTexts_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(5, 10);
        String search = "test";
        String tab = "slideText";
        when(textContentBlockRepo.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlideTexts(search,7,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_searchSlideTexts_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        String tab = "slideText";
        when(textContentBlockRepo.findInNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IVSpaceElement>(slideTexts));
        HashSet<IVSpaceElement> tempResult = serviceToTest.searchSlideTexts(search,-2,6,tab,new StringBuilder());
        assertEquals(2, tempResult.size());
        List<String> idList = new ArrayList<>();
        tempResult.forEach(x -> idList.add(x.getId()));
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findInNameOrDescription(requestedPage, search);
    }
    
    @Test
    public void test_getCountOfSearchedSlideText_success() {
        String search = "test";
        when(textContentBlockRepo.findInNameOrDescription(search)).thenReturn(6L);
        assertEquals(6L, serviceToTest.getCountOfSearchedSlideText(search));
    }
}
