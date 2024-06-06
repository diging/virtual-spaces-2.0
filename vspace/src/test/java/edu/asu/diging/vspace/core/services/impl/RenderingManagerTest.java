package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.file.impl.StorageManager;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;

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
    private SnapshotTaskRepository SnapshotTaskRepository;

    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_downloadSpace_success() throws FileStorageException {
        Space space = new Space();
        space.setId("SPACE_ID");
        space.setModuleLinks(new ArrayList());
        ModuleLink moduleLink1 = new ModuleLink();

        IModule module1 = new Module();
        moduleLink1.setId("MODULE_LINK_1");
        moduleLink1.setModule(module1);

        ModuleLink moduleLink2 = new ModuleLink();
        IModule module2 = new Module();
        moduleLink2.setId("MODULE_LINK_2");
        moduleLink2.setModule(module2);

        space.getModuleLinks().add(moduleLink1);       
        space.getModuleLinks().add(moduleLink2);  

        String exhibitionFolderPath = "/Exhibition"; 
        String spaceFolderPath = exhibitionFolderPath + File.separator+ space.getId();
        String imagesFolderPath = spaceFolderPath + File.separator+ "images";
        doNothing().when(serviceToTest).renderSpace(space.getId(), spaceFolderPath, null);
        doNothing().when(storageManager).copyImage(Mockito.any(IVSImage.class), Mockito.any(String.class));

        serviceToTest.downloadSpace(space, exhibitionFolderPath, null);

        verify(serviceToTest, times(1)).downloadModule(module1, space, imagesFolderPath, spaceFolderPath);
        verify(serviceToTest, times(1)).downloadModule(module2, space, imagesFolderPath, spaceFolderPath);
    }        

    @Test
    public void test_downloadModule_success() throws FileStorageException {
        Space space = new Space();
        space.setId("SPACE_ID");
        space.setModuleLinks(new ArrayList());
        ModuleLink moduleLink1 = new ModuleLink();

        IModule module1 = new Module();

        ISequence sequence1 = new Sequence();
        sequence1.setSlides(new ArrayList());
        module1.setStartSequence(sequence1);
        moduleLink1.setId("MODULE_LINK_1");
        moduleLink1.setModule(module1);

        space.getModuleLinks().add(moduleLink1);       

        String exhibitionFolderPath = "/Exhibition"; 
        String spaceFolderPath = exhibitionFolderPath + File.separator+ space.getId();
        String imagesFolderPath = exhibitionFolderPath + File.separator+ "images";
        serviceToTest.downloadModule(module1, space, imagesFolderPath, spaceFolderPath);

        verify(serviceToTest, times(1)).downloadSequences(sequence1, module1, space, spaceFolderPath,imagesFolderPath);
    }

    @Test
    public void test_downloadSequence_success() throws FileStorageException {
        Space space = new Space();
        space.setId("SPACE_ID");
        space.setModuleLinks(new ArrayList());
        ModuleLink moduleLink1 = new ModuleLink();

        IModule module1 = new Module();

        ISequence sequence1 = new Sequence();
        sequence1.setId("SEQ_1");
        ISequence sequence2 = new Sequence(); //for  branching point
        sequence2.setId("SEQ_2");

        sequence2.setSlides(new ArrayList());

        sequence1.setSlides(new ArrayList());
        BranchingPoint branchingPoint = new BranchingPoint();
        branchingPoint.setId("SLIDE1");
        branchingPoint.setChoices(new ArrayList());       

        Choice choice = new Choice();
        choice.setSequence(sequence2);
        branchingPoint.getChoices().add(choice);      
        sequence1.getSlides().add(branchingPoint);


        Slide slide1 = new Slide();
        slide1.setId("SLIDE2" );
        sequence1.getSlides().add(slide1);
    
        IImageBlock imageBlock = new ImageBlock();
        IVSImage image1 = new VSImage();
        imageBlock.setImage(image1);
        List<IContentBlock> list = new ArrayList();
        list.add(imageBlock);
        slide1.setContents(list);
        
        module1.setStartSequence(sequence1);


        moduleLink1.setId("MODULE_LINK_1");
        moduleLink1.setModule(module1);

        space.getModuleLinks().add(moduleLink1);       

        String exhibitionFolderPath = "/Exhibition"; 
        String spaceFolderPath = exhibitionFolderPath + File.separator+ space.getId();
        String imagesFolderPath = exhibitionFolderPath + File.separator+ "images";
        doNothing().when(serviceToTest).renderSlide(slide1.getId(), spaceFolderPath,space.getId(), module1.getId(),sequence1.getId() );

        serviceToTest.downloadSequences(sequence1, module1, space, spaceFolderPath, imagesFolderPath);

        // to test recursive call because of branching point
        verify(serviceToTest, times(1)).downloadSequences(sequence2, module1, space, spaceFolderPath,imagesFolderPath);

        //to test if html page is added for given slide
        verify(serviceToTest, times(1)).renderSlide(slide1.getId(),  spaceFolderPath, space.getId(), module1.getId(),sequence1.getId() );
    }
}
