package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import edu.asu.diging.vspace.core.data.ExhibitionDownloadRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionDownloadNotFoundException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Choice;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;


public class DownloadsManagerTest {

    @Spy 
    @InjectMocks
    private DownloadsManager serviceToTest;

    @Mock
    StorageEngine storageEngine;
    
    @Mock
    SnapshotManager snapshotManager;

    @Mock   
    ExhibitionDownloadRepository exhibitionDownloadRepo;
    
    @Mock
    SpaceRepository spaceRepository;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void test_downloadExhibitionFolder_success() throws Exception {
        ExhibitionDownload exhibitionDownload = new ExhibitionDownload();
        exhibitionDownload.setId("ID1");
        exhibitionDownload.setFolderName("Name");
        String fileContent =  "file content"; 
        byte[] byteArray = fileContent.getBytes();   
        when(exhibitionDownloadRepo.findById("ID1")).thenReturn(Optional.of(exhibitionDownload));
        when(storageEngine.generateZipFolder("Name")).thenReturn(byteArray);

        byte[] response = serviceToTest.downloadExhibitionFolder("ID1") ;
        assertEquals(response, byteArray);
    }




    @Test
    public void test_downloadExhibitionFolder_exhibitionDownloadNotPresent() {
        when(exhibitionDownloadRepo.findById("ID")).thenReturn(Optional.ofNullable(null));
        assertThrows(ExhibitionDownloadNotFoundException.class, () ->  serviceToTest.downloadExhibitionFolder("ID") );
    }
    
    
    @Test
    public void test_triggerDownloadExhibition_success() throws IOException, InterruptedException, ExecutionException {
        String resourcesPath = "/Resources";
        String exhibitionFolderName = "folderName";

        serviceToTest.triggerDownloadExhibition(resourcesPath, exhibitionFolderName);

        Mockito.verify(storageEngine).createFolder(exhibitionFolderName);

        Mockito.verify(snapshotManager).createSnapShot(resourcesPath, exhibitionFolderName, null,  null);
        
    }
  
    @Test
    public void test_checkIfSnapshotCreated_success() {

        ExhibitionDownload exhibitionDownload = new ExhibitionDownload();
        exhibitionDownload.setId("ID1");
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setTaskComplete(true);
        exhibitionDownload.setSnapshotTask(snapshotTask);
        when(exhibitionDownloadRepo.findById("ID1")).thenReturn(Optional.of(exhibitionDownload));

        assertTrue(serviceToTest.checkIfSnapshotCreated("ID1"));



    }
}
