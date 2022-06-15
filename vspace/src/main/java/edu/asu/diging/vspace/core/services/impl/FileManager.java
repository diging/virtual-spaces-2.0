package edu.asu.diging.vspace.core.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import edu.asu.diging.vspace.core.data.FileRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.IFileManager;

@Service
public class FileManager implements IFileManager {
    
    @Autowired
    private IStorageEngine storageEngine;
    
    @Autowired
    private IFileFactory fileFactory;
    
    @Autowired
    private FileRepository fileRepo;
    
    @Value("${file_uploads_directory}")
    private String fileUploadDir;
    
    @Override
    public CreationReturnValue storeFile(byte[] fileContent, String originalFileName, String fileName, String fileDescription) {
        IVSFile file = null;
        if(fileContent != null ) {
            Tika tika = new Tika();
            String contentType = tika.detect(fileContent);
            file = fileFactory.createFile(fileName, contentType);
            fileRepo.save((VSFile)file);
        }
        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        if(file != null) {
            String relativePath = null;
            try {
                relativePath = storageEngine.storeFile(fileContent, fileName, fileUploadDir);
            } catch (FileStorageException e) {
                returnValue.getErrorMsgs().add("File could not be stored: " + e);
            }
            file.setParentPath(relativePath);
            file.setFileDescription(fileDescription);
            file.setOriginalFileName(originalFileName);
            fileRepo.save((VSFile) file);
        }
        return returnValue;
        
    }
    
    @Override
    public List<VSFile> getAllFiles(){
        return fileRepo.findAll();
    }
    
    @Override
    public IVSFile getFileById(String id) {
        Optional<VSFile> optional = fileRepo.findById(id);
        return optional.get();
    }
    
    @Override
    public IVSFile editFile(String fileId, String fileName, String description) {
        Optional<VSFile> optional = fileRepo.findById(fileId);
        if(!optional.isPresent()) {
            return null;
        }
        IVSFile file  = optional.get();
        if(storageEngine.renameFile(file, fileName)) {
            file.setFilename(fileName);   
        }
        file.setFileDescription(description);
        fileRepo.save((VSFile)file);
        return file;
    }

    @Override
    public Resource downloadFile(String fileName) throws IOException {             
        return storageEngine.downloadFile(fileName);
    }
    
    @Override
    public boolean deleteFile(String fileId) {
        IVSFile file = getFileById(fileId);
        if(storageEngine.deleteFile(file)) {
            fileRepo.delete((VSFile) file);
            return true;
        }
        return false;
    }

}
