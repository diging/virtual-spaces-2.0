package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageManager;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
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
    private ExhibitionManager exhibitionManager;
    
    @Mock
    private IModuleLinkManager moduleLinkManager;
    
    @Mock
    private ISpaceLinkManager spaceLinkManager;
    
    @Mock
    private SnapshotTaskRepository SnapshotTaskRepository;
    
    @Mock
    private ISpaceDisplayManager spaceDisplayManager;
    
    @Mock
    private IExternalLinkManager externalLinkManager;
    
    @Mock
    private SpringTemplateEngine springTemplateEngine;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createSpaceSnapshot_success() throws FileStorageException {
        Space space = new Space();
        space.setId("space01");       
        IVSImage spaceImage = new VSImage();
        spaceImage.setHeight(700);
        spaceImage.setWidth(1300);
        space.setImage(spaceImage);
        
        String fileContentString = "file content";
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        Context thymeleafContext = new Context();
        
        when(spaceRepository.save((Space) space)).thenReturn(space);
        when(spaceManager.getSpace("space01")).thenReturn(space);
        //when(springTemplateEngine.process(eq(SPACE_DOWNLOAD_TEMPLATE), any(Context.class))).thenReturn(renderedContent)
        when(springTemplateEngine.process("exhibition/downloads/spaceDownloadTemplate", thymeleafContext))
        .thenReturn(fileContentString);

        String exhibitionFolderName = "exhibition1";
        SequenceHistory sequenceHistory = new SequenceHistory();

        serviceToTest.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);

        verify(storageEngine).createFolder("exhibition1/space1");
        verify(storageEngine).storeFile(Mockito.any(byte[].class), "space1.html", "exhibition1/space1");
    }        

    @Test(expected = FileStorageException.class)
    public void test_createSpaceSnapshot_folderCreationFailure() throws FileStorageException {
        Space space = new Space();
        space.setId("space01");
        IVSImage spaceImage = new VSImage();
        spaceImage.setHeight(700);
        spaceImage.setWidth(1300);
        space.setImage(spaceImage);
        
        when(space.getId()).thenReturn("space01");
        String exhibitionFolderName = "exhibition1";
        SequenceHistory sequenceHistory = mock(SequenceHistory.class);

        doThrow(FileStorageException.class).when(storageEngine).createFolder("exhibition1/space1");
        
        serviceToTest.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);       
    }

    @Test
    public void test_createSpaceSnapshot_withModuleSuccess() throws FileStorageException {
        Space space = new Space();
        space.setId("space01");
        IVSImage spaceImage = new VSImage();
        spaceImage.setHeight(700);
        spaceImage.setWidth(1300);
        space.setImage(spaceImage);
        
        when(space.getId()).thenReturn("space1");
        when(space.getImage()).thenReturn(mock(IVSImage.class));

        IModuleLink moduleLink = mock(IModuleLink.class);
        IModule module = mock(IModule.class);
        when(moduleLink.getModule()).thenReturn(module);
        when(space.getModuleLinks()).thenReturn(Collections.singletonList(moduleLink));

        String exhibitionFolderName = "exhibition1";
        SequenceHistory sequenceHistory = mock(SequenceHistory.class);

        serviceToTest.createSpaceSnapshot(space, exhibitionFolderName, sequenceHistory);

        verify(storageEngine).createFolder("exhibition1/space1");
        verify(storageEngine).storeFile(any(byte[].class), eq("space1.html"), eq("exhibition1/space1"));
        verify(storageEngine).createFolder("exhibition1/space1/images");
        verify(storageManager).copyImage(Mockito.any(IVSImage.class), eq("exhibition1/space1/images"));
        verify(moduleLink).getModule();    
    }
}
