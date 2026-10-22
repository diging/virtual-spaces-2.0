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
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.data.ExhibitionSnapshotRepository;
import edu.asu.diging.vspace.core.data.SnapshotTaskRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SnapshotCouldNotBeCreatedException;
import edu.asu.diging.vspace.core.file.impl.StorageEngine;
import edu.asu.diging.vspace.core.model.impl.ExhibitionSnapshot;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SnapshotTask;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IRenderingManager;

@RunWith(MockitoJUnitRunner.class)
public class SnapshotManagerTest {
    
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
    
    @Mock
    AsyncSnapshotCreator asyncSnapshotCreator;
    
    @Mock 
    SnapshotManager snapshotManagerTest;
    
    private String exhibitionId, snapshotTaskId, snapshotId;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        exhibitionId = "EXH000000001";
        snapshotTaskId = "SNAPTSK000000001";
        snapshotId = "SNAP000000001";
    }

    @Test
    public void test_triggerExhibitionSnapshotCreation_success() throws IOException, InterruptedException, ExecutionException, FileStorageException, SnapshotCouldNotBeCreatedException {
        String exhibitionFolderName = exhibitionId;
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        exhibitionSnapshot.setId(snapshotId);
        exhibitionSnapshot.setFolderName(exhibitionFolderName);
        
        SnapshotTask exhibitionSnapshotTask = new SnapshotTask();
        exhibitionSnapshotTask.setId(snapshotTaskId);
        exhibitionSnapshot.setSnapshotTask(exhibitionSnapshotTask);
        
        String fileContent = "fileContent";
        Future<SnapshotTask> future = CompletableFuture.completedFuture(exhibitionSnapshotTask);
        //new AsyncResult<SnapshotTask>
        
        when(exhibitionSnapshotRepo.save(any(ExhibitionSnapshot.class))).thenReturn(exhibitionSnapshot);
        when(snapshotTaskRepo.save(any(SnapshotTask.class))).thenReturn(exhibitionSnapshotTask);
        when(storageEngine.generateZip(Mockito.anyString())).thenReturn(fileContent.getBytes());
        when(asyncSnapshotCreator.createSnapshot(Mockito.anyString(), Mockito.anyString(), 
                any(SequenceHistory.class), any(ExhibitionSnapshot.class))).thenReturn(future);
        when(snapshotManagerTest.createSnapshot(Mockito.anyString(), Mockito.anyString(), 
                any(SequenceHistory.class), any(ExhibitionSnapshot.class))).thenReturn(future.get());
        serviceToTest.triggerExhibitionSnapshotCreation();
        
        assertNotNull(exhibitionSnapshot);
        assertEquals(snapshotId, exhibitionSnapshot.getId());
        verify(storageEngine).generateZip(Mockito.any(String.class));
    }
  
    @Test
    public void test_isSnapshotCreated_success() {

        ExhibitionSnapshot snapshot = new ExhibitionSnapshot();
        snapshot.setId(snapshotId);
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setTaskComplete(true);
        snapshot.setSnapshotTask(snapshotTask);
        
        when(exhibitionSnapshotRepo.findById(snapshotId)).thenReturn(Optional.of(snapshot));

        assertTrue(serviceToTest.isSnapshotCreated(snapshotId));
    }
    
    @Test
    public void test_createSnapshot_Failure() throws IOException, InterruptedException, FileStorageException {
        String resourcesPath = "/Resources";
        ExhibitionSnapshot exhibitionSnapshot = new ExhibitionSnapshot();
        exhibitionSnapshot.setId(snapshotId);
        SnapshotTask snapshotTask = new SnapshotTask();  
        snapshotTask.setExhibitionSnapshot(exhibitionSnapshot);    
        exhibitionSnapshot.setSnapshotTask(snapshotTask);

        when(spaceRepository.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(new ArrayList());
        when(asyncSnapshotCreator.createSnapshot(Mockito.anyString(), Mockito.anyString(), 
                any(SequenceHistory.class), any(ExhibitionSnapshot.class))).thenThrow(new IOException());
        doThrow(new IOException()).when(storageEngine).copyToFolder(Mockito.anyString(), Mockito.anyString() );        
        assertThrows(IOException.class, ()-> serviceToTest.createSnapshot(resourcesPath, exhibitionId, null, exhibitionSnapshot));
    }
    
    @Test
    public void test_getExhibitionSnapshot_found() throws Exception {
        SnapshotTask snapshotTask = new SnapshotTask();
        snapshotTask.setId(snapshotTaskId);

        when(snapshotTaskRepo.findByExhibitionSnapshotId(snapshotId)).thenReturn(Optional.of(snapshotTask));
        snapshotTask = serviceToTest.getSnapshotTask(snapshotId);

        assertNotNull(snapshotTask);
        assertEquals(snapshotTaskId, snapshotTask.getId());
        verify(snapshotTaskRepo).findByExhibitionSnapshotId(snapshotId);
    }

    @Test
    public void test_getExhibitionSnapshot_exhibitionDownloadNotPresent() {
        when(exhibitionSnapshotRepo.findById(snapshotId)).thenReturn(Optional.ofNullable(null));
        assertThrows(ExhibitionSnapshotNotFoundException.class, () ->  serviceToTest.getExhibitionSnapshot(snapshotId));
    }  
    
}
