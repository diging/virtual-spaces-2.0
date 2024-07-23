package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.asu.diging.vspace.core.data.ExhibitionSnapshotRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IRenderingManager;

@RunWith(MockitoJUnitRunner.class)
public class SnapshotManagerTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Spy 
    @InjectMocks
    private SnapshotManager serviceToTest;

    @Mock
    StorageEngine storageEngine;
    
    @Mock
    IRenderingManager renderingManager;

    @Mock   
    ExhibitionSnapshotRepository exhibitionSnapshotRepo;
    
    @Mock   
    SnapshotTaskRepository snapshotTaskRepo;
    
    @Mock
    SpaceRepository spaceRepository;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_triggerExhibitionSnapshotCreation_success() throws IOException, InterruptedException, ExecutionException, FileStorageException, SnapshotCouldNotBeCreatedException {
        String exhibitionFolderName = "Exhibition"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss"));;
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        exhibitionSnapshot.setId("snapshot01");
        
        SnapshotTask exhibitionSnapshotTask = new SnapshotTask();
        exhibitionSnapshotTask.setId("snapshotTask01");
        exhibitionSnapshot.setSnapshotTask(exhibitionSnapshotTask);
        
        when(exhibitionSnapshotRepo.save(any(ExhibitionSnapshot.class))).thenReturn(exhibitionSnapshot);
        when(snapshotTaskRepo.save(any(SnapshotTask.class))).thenReturn(exhibitionSnapshotTask);

        serviceToTest.triggerExhibitionSnapshotCreation();
        
        assertNotNull(exhibitionSnapshot);
        assertEquals("snapshot01", exhibitionSnapshot.getId());
        verify(storageEngine).generateZip(exhibitionFolderName);
    }
  
    @Test
    public void test_isSnapshotCreated_success() {

        ExhibitionSnapshot snapshot = new ExhibitionSnapshot();
        snapshot.setId("ID1");
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setTaskComplete(true);
        snapshot.setSnapshotTask(snapshotTask);
        when(exhibitionSnapshotRepo.findById("ID1")).thenReturn(Optional.of(snapshot));

        assertTrue(serviceToTest.isSnapshotCreated("ID1"));
    }
    
    @Test
    public void test_createSnapshot_Failure() throws IOException {
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
    
    @Test
    public void test_getExhibitionSnapshot_found() throws Exception {
        String snapshotId = "snapshot01";
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setId("snapshotTask01");

        when(snapshotTaskRepo.findByExhibitionSnapshotId(snapshotId)).thenReturn(Optional.of(snapshotTask));

        snapshotTask = serviceToTest.getSnapshotTask(snapshotId);

        // Assert
        assertNotNull(snapshotTask);
        assertEquals("snapshotTask01", snapshotTask.getId());
        verify(snapshotTaskRepo).findByExhibitionSnapshotId(snapshotId);
    }

    @Test
    public void test_getExhibitionSnapshot_exhibitionDownloadNotPresent() {
        when(exhibitionSnapshotRepo.findById("ID")).thenReturn(Optional.ofNullable(null));
        assertThrows(ExhibitionSnapshotNotFoundException.class, () ->  serviceToTest.getExhibitionSnapshot("ID") );
    }  
    
}
