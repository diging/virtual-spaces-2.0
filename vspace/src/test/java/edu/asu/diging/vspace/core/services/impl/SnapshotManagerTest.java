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

import edu.asu.diging.vspace.core.data.ExhibitionSnapshotRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public class SnapshotManagerTest {

    @Spy 
    @InjectMocks
    private SnapshotManager serviceToTest;

    @Mock
    StorageEngine storageEngine;
    
    @Mock
    RenderingManager renderingManager;

    @Mock   
    ExhibitionSnapshotRepository exhibitionSnapshotRepo;
    
    @Mock
    SpaceRepository spaceRepository;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_downloadExhibitionFolder_success() throws Exception {
        ExhibitionSnapshot exhibitionDownload = new ExhibitionSnapshot();
        exhibitionDownload.setId("ID1");
        exhibitionDownload.setFolderName("Name");
        String fileContent =  "file content"; 
        byte[] byteArray = fileContent.getBytes();   
        when(exhibitionSnapshotRepo.findById("ID1")).thenReturn(Optional.of(exhibitionDownload));
        when(storageEngine.generateZip("Name")).thenReturn(byteArray);

        byte[] response = serviceToTest.downloadExhibitionFolder("ID1") ;
        assertEquals(response, byteArray);
    }

    @Test
    public void test_downloadExhibitionFolder_exhibitionDownloadNotPresent() {
        when(exhibitionSnapshotRepo.findById("ID")).thenReturn(Optional.ofNullable(null));
        assertThrows(ExhibitionSnapshotNotFoundException.class, () ->  serviceToTest.downloadExhibitionFolder("ID") );
    }  
    
    @Test
    public void test_triggerDownloadExhibition_success() throws IOException, InterruptedException, ExecutionException, FileStorageException, SnapshotCouldNotBeCreatedException {
        String resourcesPath = "/Resources";
        String exhibitionFolderName = "folderName";
        serviceToTest.triggerExhibitionSnapshotCreation();

        Mockito.verify(storageEngine).createFolder(exhibitionFolderName);
        Mockito.verify(serviceToTest).createSnapshot(resourcesPath, exhibitionFolderName, null,  null);    
    }
  
    @Test
    public void test_checkIfSnapshotCreated_success() {

        ExhibitionSnapshot exhibitionDownload = new ExhibitionSnapshot();
        exhibitionDownload.setId("ID1");
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setTaskComplete(true);
        exhibitionDownload.setSnapshotTask(snapshotTask);
        when(exhibitionSnapshotRepo.findById("ID1")).thenReturn(Optional.of(exhibitionDownload));

        assertTrue(serviceToTest.checkIfSnapshotCreated("ID1"));
    }
    
    @Test
    public void test_downloadExhibition_createSnapShotfailure() throws IOException {
        String resourcesPath = "/Resources";
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        exhibitionSnapshot.setId("ID1");
        SnapshotTask snapshotTask = new SnapshotTask();  
        snapshotTask.setExhibitionSnapshot(exhibitionSnapshot);    
        exhibitionSnapshot.setSnapshotTask(snapshotTask);

        when(spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(new ArrayList());
        doThrow(new IOException()).when(storageEngine).copyToFolder(Mockito.anyString(), Mockito.anyString() );        
        assertThrows(IOException.class, ()-> serviceToTest.createSnapshot(resourcesPath, "folderName", null, exhibitionSnapshot));
    }
}
