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
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

public class PublicSearchManagerTest {

    @Mock
    private ISpaceManager spaceManager;

    @Mock
    private IModuleManager moduleManager;
    
    @Mock
    private IModuleLinkManager moduleLinkManager;

    @Mock
    private ISlideManager slideManager;

    @Mock
    private TextContentBlockRepository textContentBlockRepo;

    @InjectMocks
    private PublicSearchManager serviceToTest;
    

    private List<ISpace> spaces;
    
    private List<IModule> modules;
    
    private List<ISlide> slides;

    private List<ISlide> slideTexts;
    
    private List<IModuleLink> moduleLinks;
    
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
        
        Sequence sequence1 = new Sequence();
        sequence1.setId("START_SEQEUNCE_1");
        module1.setStartSequence(sequence1);
        
        Sequence sequence2 = new Sequence();
        sequence2.setId("START_SEQEUNCE_2");
        Module module2 = new Module();
        module2.setId("MODULE_ID_2");
        module2.setStartSequence(sequence2);
        
        modules = new ArrayList<>();
        modules.add(module1);
        modules.add(module2);
        
        Slide slide1 = new Slide();
        slide1.setId("SLIDE_ID_1");
        slide1.setModule(module1);
        List<IContentBlock> contentList = new ArrayList();
        contentList.add(new TextBlock());
        slide1.setContents(contentList);
        
        Slide slide2 = new Slide();
        slide2.setId("SLIDE_ID_2");
        slide2.setModule(module2);
        slide2.setContents(contentList);
    
        
        slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);
        
        Slide slideText1 = new Slide();
        slideText1.setId("SLIDETEXT_ID_1");
        slideText1.setModule(module1);
        slideText1.setContents(contentList);
        
        Slide slideText2 = new Slide();
        slideText2.setId("SLIDETEXT_ID_2");
        slideText2.setModule(module2);
        slideText2.setContents(contentList);
        
        slideTexts = new ArrayList<>();
        slideTexts.add(slideText1);
        slideTexts.add(slideText2);
        
        ModuleLink moduleLink1 = new ModuleLink();
        moduleLink1.setSpace(spaces.get(0));
        moduleLink1.setModule(modules.get(0));
        
        ModuleLink moduleLink2 = new ModuleLink();
        moduleLink2.setSpace(spaces.get(1));
        moduleLink2.setModule(modules.get(1));
        
        moduleLinks = new ArrayList();
        moduleLinks.add(moduleLink1);      
        moduleLinks.add(moduleLink2);

        
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);
    }

    @Test
    public void test_searchInSpaces_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "space";
        when(spaceManager.findBySpaceStatusAndNameOrDescription(requestedPage, SpaceStatus.PUBLISHED, search))
            .thenReturn(new PageImpl<ISpace>(spaces, requestedPage, 12));
        Page<ISpace> tempResult = serviceToTest.searchSpacesAndPaginate(search, 1);
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
            .thenReturn(new PageImpl<ISpace>(spaces,requestedPage1, 12));
        Page<ISpace> tempResult = serviceToTest.searchSpacesAndPaginate(search, 7);
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
        Page<ISpace> tempResult = serviceToTest.searchSpacesAndPaginate(search, -1);
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
        when(moduleManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<IModule>(modules));
  
        
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<IModule> tempResult = serviceToTest.searchModulesAndPaginate(search, 1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
    }

    @Test
    public void test_searchInModules_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<IModule>(new ArrayList<IModule>()));
       
        
        Pageable requestedPage1 = PageRequest.of(0, 10);
        when(moduleManager.findByNameOrDescriptionLinkedToSpace(requestedPage1, search)).thenReturn(new PageImpl<IModule>(modules));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<IModule> tempResult = serviceToTest.searchModulesAndPaginate(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
        verify(moduleManager).findByNameOrDescriptionLinkedToSpace(requestedPage1, search);
    }

    @Test
    public void test_searchInModules_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "module";
        when(moduleManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<IModule>(modules));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<IModule> tempResult = serviceToTest.searchModulesAndPaginate(search, -2);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(module -> module.getId()).collect(Collectors.toList());
        assertEquals("MODULE_ID_1", idList.get(0));
        assertEquals("MODULE_ID_2", idList.get(1));
        verify(moduleManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
    }

    @Test
    public void test_searchInSlides_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";

        when(slideManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<ISlide>(slides));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<ISlide> tempResult = serviceToTest.searchSlidesAndPaginate(search, 1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
    }

    @Test
    public void test_searchInSlides_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<ISlide>(new ArrayList<ISlide>()));
        Pageable requestedPage1 = PageRequest.of(0, 10);
        when(slideManager.findByNameOrDescriptionLinkedToSpace(requestedPage1, search)).thenReturn(new PageImpl<ISlide>(slides));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<ISlide> tempResult = serviceToTest.searchSlidesAndPaginate(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
        verify(slideManager).findByNameOrDescriptionLinkedToSpace(requestedPage1, search);
    }

    @Test
    public void test_searchInSlides_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "slide";
        when(slideManager.findByNameOrDescriptionLinkedToSpace(requestedPage, search)).thenReturn(new PageImpl<ISlide>(slides));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<ISlide> tempResult = serviceToTest.searchSlidesAndPaginate(search, -2);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDE_ID_1", idList.get(0));
        assertEquals("SLIDE_ID_2", idList.get(1));
        verify(slideManager).findByNameOrDescriptionLinkedToSpace(requestedPage, search);
    }

    @Test
    public void test_searchInSlideTexts_success() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED))
                .thenReturn(new PageImpl<ISlide>(slideTexts));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<ISlide> tempResult = serviceToTest.searchSlideTextsAndPaginate(search, 1);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED);
    }

    @Test
    public void test_searchInSlideTexts_pageGreaterThanTotalPages() {
        Pageable requestedPage = PageRequest.of(6, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED))
                .thenReturn(new PageImpl<ISlide>(new ArrayList<ISlide>()));
        Pageable requestedPage1 = PageRequest.of(0, 10);
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        when(textContentBlockRepo.findWithNameOrDescriptionLinkedToSpace(requestedPage1, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED))
                .thenReturn(new PageImpl<ISlide>(slideTexts));
        Page<ISlide> tempResult = serviceToTest.searchSlideTextsAndPaginate(search, 7);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED);
        verify(textContentBlockRepo).findWithNameOrDescriptionLinkedToSpace(requestedPage1, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED);
    }

    @Test
    public void test_searchInSlideTexts_negativePage() {
        Pageable requestedPage = PageRequest.of(0, 10);
        String search = "test";
        when(textContentBlockRepo.findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED))
                .thenReturn(new PageImpl<ISlide>(slideTexts));
        when(moduleLinkManager.findFirstByModule(modules.get(0))).thenReturn((ModuleLink) moduleLinks.get(0));
        when(moduleLinkManager.findFirstByModule(modules.get(1))).thenReturn((ModuleLink) moduleLinks.get(1));
        Page<ISlide> tempResult = serviceToTest.searchSlideTextsAndPaginate(search, -2);
        assertEquals(2, tempResult.getContent().size());
        List<String> idList = tempResult.stream().map(slide -> slide.getId()).collect(Collectors.toList());
        assertEquals("SLIDETEXT_ID_1", idList.get(0));
        assertEquals("SLIDETEXT_ID_2", idList.get(1));
        verify(textContentBlockRepo).findWithNameOrDescriptionLinkedToSpace(requestedPage, search, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED);
    }
   
}
