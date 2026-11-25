package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SequenceNotFoundException;
import edu.asu.diging.vspace.core.exception.SlidesInSequenceNotFoundException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageManager;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;

@RunWith(MockitoJUnitRunner.class)
public class RenderingManagerTest {
    
    @Spy 
    @InjectMocks
    private RenderingManager serviceToTest;
        
    @Spy
    private FileUtils fileUtils;

    @Mock
    private StorageEngine storageEngine;
    
    @Mock
    private SpaceRepository spaceRepository;
    
    @Mock
    private StorageManager storageManager;
    
    @Mock
    private ISpaceManager spaceManager;
    
    @Mock
    private IModuleManager moduleManager;
    
    @Mock
    private ExhibitionManager exhibitionManager;
    
    @Mock
    private IModuleLinkManager moduleLinkManager;
    
    @Mock
    private ISpaceLinkManager spaceLinkManager;
    
    @Mock
    private SequenceRepository sequenceRepo;
    
    @Mock
    private SnapshotTaskRepository SnapshotTaskRepository;
    
    @Mock
    private ISpaceDisplayManager spaceDisplayManager;
    
    @Mock
    private ISnapshotManager snapshotManager;
    
    @Mock
    private IExternalLinkManager externalLinkManager;
    
    @Mock
    private ISpaceTextBlockManager spaceTextBlockManager;

    @Mock
    private ISequenceManager sequenceManager;

    @Mock
    private ISlideManager slideManager;
    
    @Mock
    private TemplateEngine templateEngine;
    
    @Spy
    private SpringTemplateEngine springTemplateEngine;
    
    private String exhibitionId, moduleId, sequenceId, spaceId, slide1Id, slide2Id;
    
    private Space space;
    private IModule module;
    private ISlide slide1;
    private ISequence sequence;
    private List<IModuleLink> moduleLinks;
    private List<ISlide> slides;
    
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        exhibitionId = "EXH000000001";
        spaceId = "SPA000000001";
        moduleId = "MOD000000001";
        sequenceId = "SEQ000000001";
        slide1Id = "SLI000000001";
        slide2Id = "SLI000000002";
        
        space = new Space();
        space.setId(spaceId);
        module = new Module();
        module.setId(moduleId);
        sequence = new Sequence();
        sequence.setId(sequenceId);
        module.setStartSequence(sequence);
        
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);
        moduleLink.setModule(module);
        moduleLinks = new ArrayList<IModuleLink>();
        moduleLinks.add(moduleLink);
        space.setModuleLinks(moduleLinks);
        
        slide1 = new Slide();
        slide1.setId(slide1Id);
        slide1.setContents(new ArrayList<IContentBlock>()); 
        slides = new ArrayList<>();
        slides.add(slide1);
        sequence.setSlides(slides);

    }

    @Test
    public void test_createSpaceSnapshot_success() throws FileStorageException, ExhibitionSnapshotNotFoundException, IOException {

        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        serviceToTest.createSpaceSnapshot(space, exhibitionId);
        
        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId);
        verify(storageEngine).storeFile(Mockito.any(byte[].class), eq(spaceId + ".html"), eq(exhibitionId + File.separator + spaceId));
    }        

    @Test
    public void test_createSpaceSnapshot_folderCreationFailure() throws FileStorageException {     
        Slide slide2 = new Slide();
        slide2.setId(slide2Id);
        
        slide1.setContents(new ArrayList<IContentBlock>());
        slide2.setContents(new ArrayList<IContentBlock>());
 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);

        space.setModuleLinks(moduleLinks);
        sequence.setSlides(slides);
        String exhibitionFolderName = exhibitionId;
        Space space2 = new Space();
        space.setId("space02"); 
        
        
        doThrow(FileStorageException.class).when(serviceToTest).createSpaceSnapshot(space2, exhibitionFolderName);
        assertThrows(FileStorageException.class, () -> serviceToTest.createSpaceSnapshot(space2, exhibitionFolderName));
    }

    @Test
    public void test_createSpaceSnapshot_withModuleSuccess() throws FileStorageException {  
        Slide slide2 = new Slide();
        slide2.setId(slide2Id);
        
        slide1.setContents(new ArrayList<IContentBlock>());
        slide2.setContents(new ArrayList<IContentBlock>());
 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);
        slides.add(slide2);

        space.setModuleLinks(moduleLinks);
        sequence.setSlides(slides);
        String exhibitionFolderName = exhibitionId;
        VSImage spaceImage = new VSImage();
        spaceImage.setId("IMG000000001");
        space.setImage(spaceImage);

        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);

        serviceToTest.createSpaceSnapshot(space, exhibitionFolderName);

        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId);
        verify(storageEngine).storeFile(any(byte[].class), eq(spaceId + ".html"), eq(exhibitionId + File.separator + spaceId));
        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId + File.separator +"images");
        verify(storageManager).copyImage(eq(spaceImage), eq(exhibitionId + File.separator + spaceId+ File.separator + "images"));
    }

    @Test
    public void test_createSpaceSnapshot_skipsLinksWithoutTargetSpace() throws FileStorageException {
        Space targetSpace = new Space();
        targetSpace.setId("SPA000000002");
        targetSpace.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<ISpaceLinkDisplay> linkDisplays = new ArrayList<>();
        linkDisplays.add(createSpaceLinkDisplay("SPL000000001", targetSpace));
        // link whose target space was deleted
        linkDisplays.add(createSpaceLinkDisplay("SPL000000002", null));

        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        when(spaceLinkManager.getLinkDisplays(spaceId)).thenReturn(linkDisplays);

        serviceToTest.createSpaceSnapshot(space, exhibitionId);

        ArgumentCaptor<IContext> contextCaptor = ArgumentCaptor.forClass(IContext.class);
        verify(springTemplateEngine, Mockito.atLeastOnce()).process(Mockito.anyString(), contextCaptor.capture());
        List<?> spaceLinks = (List<?>) contextCaptor.getAllValues().get(0).getVariable("spaceLinks");
        assertEquals(1, spaceLinks.size());
        assertEquals("SPL000000001", ((ISpaceLinkDisplay) spaceLinks.get(0)).getLink().getId());
    }

    @Test
    public void test_createSpaceSnapshot_missingImageDoesNotStopSnapshot() throws FileStorageException {
        VSImage spaceImage = new VSImage();
        spaceImage.setId("IMG000000001");
        space.setImage(spaceImage);
        String imagesFolder = exhibitionId + File.separator + spaceId + File.separator + "images";

        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        doThrow(FileStorageException.class).when(storageManager).copyImage(spaceImage, imagesFolder);

        serviceToTest.createSpaceSnapshot(space, exhibitionId);

        verify(storageEngine).storeFile(any(byte[].class), eq(spaceId + ".html"), eq(exhibitionId + File.separator + spaceId));
        verify(storageEngine).storeFile(any(byte[].class), eq(slide1Id + ".html"), eq(exhibitionId + File.separator + spaceId));
    }

    @Test
    public void test_createSpaceSnapshot_exportsDefaultLinkImages() throws FileStorageException {
        VSImage moduleDefaultImage = new VSImage();
        moduleDefaultImage.setId("IMG000000002");
        moduleDefaultImage.setFilename("module.png");
        VSImage spaceDefaultImage = new VSImage();
        spaceDefaultImage.setId("IMG000000003");
        spaceDefaultImage.setFilename("space.png");
        Exhibition exhibition = new Exhibition();
        exhibition.setModuleLinkDefaultImage(moduleDefaultImage);
        exhibition.setSpaceLinkDefaultImage(spaceDefaultImage);
        exhibition.disableSpaceLinkDefaultImage();

        ModuleLinkDisplay moduleLinkDisplay = new ModuleLinkDisplay();
        moduleLinkDisplay.setLink((ModuleLink) moduleLinks.get(0));
        moduleLinkDisplay.setType(DisplayType.ARROW);
        List<IModuleLinkDisplay> moduleLinkDisplays = new ArrayList<>();
        moduleLinkDisplays.add(moduleLinkDisplay);
        String imagesFolder = exhibitionId + File.separator + spaceId + File.separator + "images";

        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        when(moduleLinkManager.getLinkDisplays(spaceId)).thenReturn(moduleLinkDisplays);

        serviceToTest.createSpaceSnapshot(space, exhibitionId);

        verify(storageManager).copyImage(moduleDefaultImage, imagesFolder);
        verify(storageManager, Mockito.never()).copyImage(spaceDefaultImage, imagesFolder);
        ArgumentCaptor<IContext> contextCaptor = ArgumentCaptor.forClass(IContext.class);
        verify(springTemplateEngine, Mockito.atLeastOnce()).process(Mockito.anyString(), contextCaptor.capture());
        List<?> linkMarkers = (List<?>) contextCaptor.getAllValues().get(0).getVariable("linkMarkers");
        assertEquals(1, linkMarkers.size());
        assertEquals("./images/IMG000000002/module.png", ((Map<?, ?>) linkMarkers.get(0)).get("defaultImageSrc"));
    }

    @Test
    public void test_createSpaceSnapshot_setsExhibitionForNavbarTitle() throws FileStorageException,
            SlidesInSequenceNotFoundException, SequenceNotFoundException {
        Exhibition exhibition = new Exhibition();
        exhibition.setTitle("Test Exhibition");

        when(exhibitionManager.getStartExhibition()).thenReturn(exhibition);
        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        when(moduleManager.checkIfSequenceExists(moduleId, sequenceId)).thenReturn(sequence);
        when(sequenceManager.getSequence(sequenceId)).thenReturn(sequence);
        when(slideManager.getSlide(slide1Id)).thenReturn(slide1);

        serviceToTest.createSpaceSnapshot(space, exhibitionId);

        ArgumentCaptor<String> templateCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<IContext> contextCaptor = ArgumentCaptor.forClass(IContext.class);
        verify(springTemplateEngine, Mockito.atLeast(2)).process(templateCaptor.capture(), contextCaptor.capture());
        assertEquals("exhibition/downloads/spaceDownloadTemplate", templateCaptor.getAllValues().get(0));
        assertEquals("exhibition/downloads/slideDownloadTemplate", templateCaptor.getAllValues().get(1));
        assertEquals(exhibition, contextCaptor.getAllValues().get(0).getVariable("exhibition"));
        assertEquals(exhibition, contextCaptor.getAllValues().get(1).getVariable("exhibition"));
    }

    private ISpaceLinkDisplay createSpaceLinkDisplay(String linkId, Space targetSpace) {
        SpaceLink link = new SpaceLink();
        link.setId(linkId);
        link.setSourceSpace(space);
        link.setTargetSpace(targetSpace);
        SpaceLinkDisplay display = new SpaceLinkDisplay();
        display.setLink(link);
        return display;
    }
}
