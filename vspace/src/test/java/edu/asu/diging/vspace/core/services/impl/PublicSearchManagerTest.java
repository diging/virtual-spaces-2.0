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
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class PublicSearchManagerTest {

    @Mock
    private ISpaceManager spaceManager;

    @Mock
    private IModuleManager moduleManager;

    @Mock
    private ISlideManager slideManager;

    @Mock
    private TextContentBlockRepository textContentBlockRepo;

    @InjectMocks
    private PublicSearchManager serviceToTest;

    private List<ISpace> spaces;
    
    private List<ISpace> lastPageSpaces;

    private List<IModule> modules;
    
    private List<IModule> lastPageModules;

    private List<ISlide> slides;
    
    private List<ISlide> lastPageSlides;

    private List<ISlide> slideTexts;
    
    private List<ISlide> lastPageSlideTexts;

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
        
        lastPageSpaces = new ArrayList<>();
        lastPageSpaces.add(space1);
        lastPageSpaces.add(space2);
        
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
    public void test_searchInSpaces_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search))
            .thenReturn(new PageImpl<ISpace>(spaces, requestedPage, 12));
        Page<ISpace> tempResult = serviceToTest.searchInSpaces(search, 1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search);
    }

    @Test
    public void test_searchInSpaces_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "space";
        when(spaceManager.findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search))
            .thenReturn(new PageImpl<ISpace>(new ArrayList<ISpace>(),requestedPage, 12));
        Pageable requestedPage1 = PageRequest.of(1, 10);
        when(spaceManager.findBySpaceStatusAndNameOrDescription(requestedPage1, SpaceStatus.PUBLISHED, search))
            .thenReturn(new PageImpl<ISpace>(lastPageSpaces,requestedPage1, 12));
        Page<ISpace> tempResult = serviceToTest.searchInSpaces(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search);
        verify(spaceManager).findBySpaceStatusAndNameOrDescription(requestedPage1, SpaceStatus.PUBLISHED, search);
    }

    @Test
    public void test_searchInSpaces_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search))
            .thenReturn(new PageImpl<ISpace>(spaces, requestedPage, 12));
        Page<ISpace> tempResult = serviceToTest.searchInSpaces(search, -1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(space -> space.getId()).collect(Collectors.toList());
        assertEquals("SPACE_ID_1", idList.get(0));
        assertEquals("SPACE_ID_2", idList.get(1));
        verify(spaceManager).findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search);
    }

    @Test
    public void test_searchInModules_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<IModule>(modules));
        Page<IModule> tempResult = serviceToTest.searchInModules(search, 1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findByNameOrDescription(requestedPage, search);
    }

//    @Test
//    public void test_searchInModules_pageGreaterThanTotalPages() {
//        Pageable requestedPage = PageRequest.of(6, 10);
//        String search = "module";
//        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Module>(modules));
//        Pageable requestedPage1 = PageRequest.of(0, 10);
//        when(moduleManager.findByNameOrDescription(requestedPage1, search)).thenReturn(new PageImpl<Module>(modules));
//        Page<Module> tempResult = serviceToTest.searchInModules(search, 7);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
//        assertEquals("MODULE_ID_1", idList.get(0));
//        assertEquals("MODULE_ID_2", idList.get(1));
//        verify(moduleManager).findByNameOrDescription(requestedPage, search);
//        verify(moduleManager).findByNameOrDescription(requestedPage1, search);
//    }
//
//    @Test
//    public void test_searchInModules_negativePage() {
//        Pageable requestedPage = PageRequest.of(0, 10);
//        String search = "module";
//        when(moduleManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Module>(modules));
//        Page<Module> tempResult = serviceToTest.searchInModules(search, -2);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
//        assertEquals("MODULE_ID_1", idList.get(0));
//        assertEquals("MODULE_ID_2", idList.get(1));
//        verify(moduleManager).findByNameOrDescription(requestedPage, search);
//    }
//
//    @Test
//    public void test_searchInSlides_success() {
//        Pageable requestedPage = PageRequest.of(0, 10);
//        String search = "slide";
//        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(slides));
//        Page<Slide> tempResult = serviceToTest.searchInSlides(search, 1);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDE_ID_1", idList.get(0));
//        assertEquals("SLIDE_ID_2", idList.get(1));
//        verify(slideManager).findByNameOrDescription(requestedPage, search);
//    }
//
//    @Test
//    public void test_searchInSlides_pageGreaterThanTotalPages() {
//        Pageable requestedPage = PageRequest.of(6, 10);
//        String search = "slide";
//        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(slides));
//        Pageable requestedPage1 = PageRequest.of(0, 10);
//        when(slideManager.findByNameOrDescription(requestedPage1, search)).thenReturn(new PageImpl<Slide>(slides));
//        Page<Slide> tempResult = serviceToTest.searchInSlides(search, 7);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDE_ID_1", idList.get(0));
//        assertEquals("SLIDE_ID_2", idList.get(1));
//        verify(slideManager).findByNameOrDescription(requestedPage, search);
//        verify(slideManager).findByNameOrDescription(requestedPage1, search);
//    }
//
//    @Test
//    public void test_searchInSlides_negativePage() {
//        Pageable requestedPage = PageRequest.of(0, 10);
//        String search = "slide";
//        when(slideManager.findByNameOrDescription(requestedPage, search)).thenReturn(new PageImpl<Slide>(slides));
//        Page<Slide> tempResult = serviceToTest.searchInSlides(search, -2);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDE_ID_1", idList.get(0));
//        assertEquals("SLIDE_ID_2", idList.get(1));
//        verify(slideManager).findByNameOrDescription(requestedPage, search);
//    }
//
//    @Test
//    public void test_searchInSlideTexts_success() {
//        Pageable requestedPage = PageRequest.of(0, 10);
//        String search = "test";
//        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
//                .thenReturn(new PageImpl<Slide>(slideTexts));
//        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, 1);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDETEXT_ID_1", idList.get(0));
//        assertEquals("SLIDETEXT_ID_2", idList.get(1));
//        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
//    }
//
//    @Test
//    public void test_searchInSlideTexts_pageGreaterThanTotalPages() {
//        Pageable requestedPage = PageRequest.of(6, 10);
//        String search = "test";
//        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
//                .thenReturn(new PageImpl<Slide>(slideTexts));
//        Pageable requestedPage1 = PageRequest.of(0, 10);
//        when(textContentBlockRepo.findWithNameOrDescription(requestedPage1, search))
//                .thenReturn(new PageImpl<Slide>(slideTexts));
//        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, 7);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDETEXT_ID_1", idList.get(0));
//        assertEquals("SLIDETEXT_ID_2", idList.get(1));
//        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
//        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage1, search);
//    }
//
//    @Test
//    public void test_searchInSlideTexts_negativePage() {
//        Pageable requestedPage = PageRequest.of(0, 10);
//        String search = "test";
//        when(textContentBlockRepo.findWithNameOrDescription(requestedPage, search))
//                .thenReturn(new PageImpl<Slide>(slideTexts));
//        Page<Slide> tempResult = serviceToTest.searchInSlideTexts(search, -2);
//        assertEquals(2, tempResult.getContent().size());
//        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
//        assertEquals("SLIDETEXT_ID_1", idList.get(0));
//        assertEquals("SLIDETEXT_ID_2", idList.get(1));
//        verify(textContentBlockRepo).findWithNameOrDescription(requestedPage, search);
//    }
   
}
