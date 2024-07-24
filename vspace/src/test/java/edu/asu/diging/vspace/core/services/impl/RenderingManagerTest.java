package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageManager;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISnapshotManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

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
    private TemplateEngine templateEngine;
    
    @Spy
    private SpringTemplateEngine springTemplateEngine;
    
    private String exhibitionId, moduleId, sequenceId, spaceId, slide1Id, slide2Id, snapshotId;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        exhibitionId = "EXH000000001";
        spaceId = "SPA000000001";
        moduleId = "MOD000000001";
        sequenceId = "SEQ000000001";
        slide1Id = "SLI000000001";
        slide2Id = "SLI000000002";
        snapshotId = "SNAP000000001";
    }

    @Test
    public void test_createSpaceSnapshot_success() throws FileStorageException, ExhibitionSnapshotNotFoundException, IOException {
        Space space = new Space();
        space.setId(spaceId);
        
        ISequence newSequence = new Sequence();
        newSequence.setId(sequenceId);
        
        IModule module = new Module();
        module.setId(moduleId);
        module.setStartSequence(newSequence);
        
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("MOL001");
        moduleLink.setSpace(space);
        moduleLink.setModule(module);
        List<IModuleLink> moduleLinks = new ArrayList<IModuleLink>();
        moduleLinks.add(moduleLink);
        
        Slide slide1 = new Slide();
        slide1.setId(slide1Id);
        slide1.setContents(new ArrayList<IContentBlock>()); 
        List<ISlide> slides = new ArrayList<>();
        slides.add(slide1);

        space.setModuleLinks(moduleLinks);
        newSequence.setSlides(slides);
        SequenceHistory sequenceHistory = new SequenceHistory();

        when(spaceRepository.save((Space) space)).thenReturn(space);
        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module);
        serviceToTest.createSpaceSnapshot(space, exhibitionId, sequenceHistory);
        
        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId);
        verify(storageEngine).storeFile(Mockito.any(byte[].class), eq(spaceId + ".html"), eq(exhibitionId + File.separator + spaceId));
    }        

    @Test
    public void test_createSpaceSnapshot_folderCreationFailure() throws FileStorageException {
        Space space = new Space();
        space.setId(spaceId);
        
        Sequence sequence = new Sequence();
        sequence.setId(sequenceId);
        
        Module module = new Module();
        module.setId(moduleId);
        module.setStartSequence(sequence);
        
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("modl001");
        moduleLink.setSpace(space);
        moduleLink.setModule(module);
        List<IModuleLink> moduleLinks = new ArrayList<IModuleLink>();
        moduleLinks.add(moduleLink);
        
        Slide slide1 = new Slide();
        slide1.setId(slide1Id);       
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
        SequenceHistory sequenceHistory = mock(SequenceHistory.class);
        Space space2 = new Space();
        space.setId("space02"); 
        
        when(spaceRepository.save((Space) space)).thenReturn(space);
        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        
        doThrow(FileStorageException.class).when(serviceToTest).createSpaceSnapshot(space2, exhibitionFolderName, sequenceHistory);
        assertThrows(FileStorageException.class, () -> serviceToTest.createSpaceSnapshot(space2, exhibitionFolderName, sequenceHistory));
    }

    @Test
    public void test_createSpaceSnapshot_withModuleSuccess() throws FileStorageException {
        Space space = new Space();
        space.setId(spaceId);
        
        Sequence sequence = new Sequence();
        sequence.setId(sequenceId);
        
        Module module = new Module();
        module.setId(moduleId);
        module.setStartSequence(sequence);
        
        IModuleLink moduleLink = new ModuleLink();
        moduleLink.setId("modl001");
        moduleLink.setSpace(space);
        moduleLink.setModule(module);
        List<IModuleLink> moduleLinks = new ArrayList<IModuleLink>();
        moduleLinks.add(moduleLink);
        
        Slide slide1 = new Slide();
        slide1.setId(slide1Id);       
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
        SequenceHistory sequenceHistory = mock(SequenceHistory.class);
        
        when(spaceRepository.save((Space) space)).thenReturn(space);
        when(spaceManager.getSpace(spaceId)).thenReturn(space);
        when(moduleManager.getModule(moduleId)).thenReturn(module); 

        serviceToTest.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);

        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId);
        verify(storageEngine).storeFile(any(byte[].class), eq(spaceId + ".html"), eq(exhibitionId + File.separator + spaceId));
        verify(storageEngine).createFolder(exhibitionId + File.separator + spaceId + File.separator +"images");
        verify(storageManager).copyImage(Mockito.any(IVSImage.class), eq(exhibitionId + File.separator + spaceId+ File.separator + "images"));
    }
}
