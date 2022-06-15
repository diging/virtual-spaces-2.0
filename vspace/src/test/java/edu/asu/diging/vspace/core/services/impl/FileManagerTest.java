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
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.FileRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;

public class FileManagerTest {
    
    @Mock
    private IStorageEngine storageEngine;
    
    @Mock
    private IFileFactory fileFactory;
    
    @Mock
    private FileRepository fileRepo;
    
    @InjectMocks
    private FileManager serviceToTest;
    
    private final String fileId = "FILE_ID";
    private final String fileName = "fileName";
    private final String fileDescription = "fileName";
    private final String fileContentString = "file content";
    
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_getFileById_success() {
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        serviceToTest.getFileById(fileId);
        verify(fileRepo).findById(fileId);
    }
    
    @Test
    public void test_getAllFiles_success() {
        serviceToTest.getAllFiles();
        verify(fileRepo).findAll();
    }
    
    @Test
    public void test_editFile_success() {
        VSFile file = new VSFile();
        file.setFilename(fileName);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.renameFile(file, fileName)).thenReturn(true);
        IVSFile returnedFile = serviceToTest.editFile(fileId, fileName, fileDescription);
        assertEquals(file.getFilename(), returnedFile.getFilename());
    }
    
    @Test
    public void test_deleteFile_success() {
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.deleteFile(file)).thenReturn(true);
        serviceToTest.deleteFile(fileId);
        verify(fileRepo).delete(file);
    }
    
    @Test
    public void test_storeFile_success() throws FileStorageException {
        String fileId = "fileId";
        VSFile file = new VSFile();
        String relativePath = "relativePath";
        byte[] fileBytes = fileContentString.getBytes();
        when(storageEngine.deleteFile(file)).thenReturn(true);
        when(fileFactory.createFile(fileName, fileId)).thenReturn(file);
        when(storageEngine.storeFile(fileBytes, fileName, fileContentString)).thenReturn(relativePath);
        CreationReturnValue returnValue = serviceToTest.storeFile(fileBytes, fileName, fileDescription, fileId);
        verify(fileFactory).createFile(fileName, "text/plain");
    }
    
    @Test
    public void test_storeFile_failure() throws FileStorageException {
        
        String fileId = "fileId";
        VSFile file = new VSFile();
        byte[] fileBytes = fileContentString.getBytes();
        when(storageEngine.deleteFile(file)).thenReturn(true);
        when(fileFactory.createFile(fileName, "text/plain")).thenReturn(file);
        when(storageEngine.storeFile(fileBytes, fileName, null)).thenThrow(new FileStorageException());
        CreationReturnValue returnValue = serviceToTest.storeFile(fileBytes, fileName, fileDescription, fileId);

        Assert.assertTrue(returnValue.getErrorMsgs().get(0).contains("File could not be stored: "));
        verify(fileFactory).createFile(fileName, "text/plain");
    }
    
    @Test
    public void test_deleteFile_failure() {
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.deleteFile(file)).thenReturn(false);
        assertFalse(serviceToTest.deleteFile(fileId));
    }
    
    @Test
    public void test_editFile_failure() {
        VSFile file = new VSFile();
        file.setFilename(fileName);
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.renameFile(file, fileName)).thenReturn(true);
        IVSFile returnedFile = serviceToTest.editFile(fileId, fileName, fileDescription);
        assertEquals(file.getFilename(), returnedFile.getFilename());
    }
    @Test
    public void test_downloadFile_failure() throws IOException {
        String fileId = "fileId";
        VSFile file = new VSFile();
        when(fileRepo.findById(fileId)).thenReturn(Optional.of(file));
        when(storageEngine.downloadFile(Mockito.any(String.class))).thenThrow(new IOException());
        assertThrows(IOException.class, () -> serviceToTest.downloadFile(fileId));
    }
}
