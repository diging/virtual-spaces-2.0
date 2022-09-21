package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.data.FileRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class FileManagerTest {
    
    @Mock
    private IStorageEngine storageEngine;
    
    @Mock
    private IFileFactory fileFactory;
    
    @Mock
    private FileRepository fileRepo;
    
    @InjectMocks
    private FileManager serviceToTest;
    
    final String fileId = "FILE_ID";
    private final String fileName = "fileName";
    private final String fileDescription = "fileName";
    private final String fileContentString = "file content";
       
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);

    }
    
    @Test
    public void test_getFileById_success() {
        VSFile file = new VSFile();
        file.setId(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        IVSFile fileResponse = serviceToTest.getFileById(fileId);
        assertEquals(fileResponse.getId(), "FILE_ID");
        
    }
    
    @Test
    public void test_getFileById_failure() {
        VSFile file = new VSFile();
        file.setId(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.ofNullable(null));
        IVSFile fileResponse = serviceToTest.getFileById(fileId);
        assertEquals(fileResponse, null);
        
    }
    
    @Test
    public void test_getAllFiles_success() {
        List<VSFile> files = new ArrayList();
        VSFile file = new VSFile();
        file.setId(fileId);
        files.add(file);
        Pageable requestedPageForFiles = PageRequest.of(0, 10);
        when(fileRepo.findAll(requestedPageForFiles)).thenReturn( new PageImpl<VSFile>(files));
        Page<IVSFile> filesResponse = serviceToTest.getAllFiles(1);
        assertEquals(filesResponse.getNumberOfElements(), 1);
        assertEquals(filesResponse.getContent().get(0).getId(), fileId);
    }
    
    @Test
    public void test_getAllFiles_failure() {
        Pageable requestedPageForFiles = PageRequest.of(0, 10);
        when(fileRepo.findAll(requestedPageForFiles)).thenReturn( new PageImpl<VSFile>(new ArrayList()));
        Page<IVSFile> filesResponse = serviceToTest.getAllFiles(1);
        assertEquals(filesResponse.getNumberOfElements(), 0);
    }
    
    @Test
    public void test_editFile_success() {
        VSFile file = new VSFile();
        file.setFilename(fileName);
        file.setId(fileId);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.renameFile(file.getFilename(), "NEW_FILENAME", fileId)).thenReturn(true);
        IVSFile returnedFile = serviceToTest.editFile(fileId, "NEW_FILENAME", fileDescription);                
        assertEquals("NEW_FILENAME", returnedFile.getFilename());
        assertEquals(fileDescription, returnedFile.getFileDescription());
    }
    
    @Test
    public void test_editFile_failure() {
        VSFile file = new VSFile();
        file.setFilename(fileName);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.renameFile(file.getFilename(), "NEW_FILENAME", fileId)).thenReturn(false);
        IVSFile returnedFile = serviceToTest.editFile(fileId, "NEW_FILENAME", fileDescription);
        assertEquals(file.getFilename(), returnedFile.getFilename());
    }
    
    @Test
    public void test_deleteFile_success() {
        VSFile file = new VSFile();
        file.setFilename(fileName);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.deleteFile(file.getFilename(), fileId)).thenReturn(true);
        serviceToTest.deleteFile(fileId);
        verify(fileRepo).delete(file);
    }
    
    @Test
    public void test_deleteFile_failure() {
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.deleteFile(file.getFilename(), "")).thenReturn(false);
        assertFalse(serviceToTest.deleteFile(fileId));
    }
    
    @Test
    public void test_storeFile_success() throws FileStorageException {
        String fileId = "fileId";
        VSFile file = new VSFile();
        file.setId(fileId);
        String relativePath = "relativePath";
        byte[] fileBytes = fileContentString.getBytes();
        when(storageEngine.deleteFile(file.getFilename(), null)).thenReturn(true);
        when(fileFactory.createFile(fileName, "text/plain")).thenReturn(file);
        when(storageEngine.storeFile(fileBytes, fileName, fileContentString)).thenReturn(relativePath);
        CreationReturnValue returnValue = serviceToTest.storeFile(fileBytes, fileName, fileDescription, fileId);
        verify(fileFactory).createFile(fileName, "text/plain");
        assertEquals(returnValue.getElement().getId(), fileId);
    }
    
    @Test
    public void test_storeFile_failure() throws FileStorageException {
        
        String fileId = "fileId";
        VSFile file = new VSFile();
        byte[] fileBytes = fileContentString.getBytes();
        when(storageEngine.deleteFile(file.getFilename(), null)).thenReturn(true);
        when(fileFactory.createFile(fileName, "text/plain")).thenReturn(file);
        when(storageEngine.storeFile(fileBytes, fileName, null)).thenThrow(new FileStorageException());
        CreationReturnValue returnValue = serviceToTest.storeFile(fileBytes, fileName, fileDescription, fileId);

        Assert.assertTrue(returnValue.getErrorMsgs().get(0).contains("File could not be stored: "));
    }
   
    @Test
    public void test_downloadFile_success() throws IOException {
        String fileId = "fileId";
        VSFile vsFile = new VSFile();
        when(storageEngine.downloadFile(Mockito.any(String.class), Mockito.any( String.class))).thenReturn(new ByteArrayResource(fileContentString.getBytes()));      
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(vsFile));
        Resource fileResponse = serviceToTest.downloadFile("filename", fileId);
        assertEquals(fileResponse ,new ByteArrayResource(fileContentString.getBytes()) );
            
    }
    
    @Test
    public void test_downloadFile_failure() throws IOException {
        String fileId = "fileId";
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.downloadFile(Mockito.any(String.class) ,Mockito.any( String.class))).thenThrow(new IOException());
        assertThrows(IOException.class, () -> serviceToTest.downloadFile(file.getFilename(), fileId));
    }
}
