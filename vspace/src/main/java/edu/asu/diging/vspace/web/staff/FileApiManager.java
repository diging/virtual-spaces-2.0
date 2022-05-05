package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

import edu.asu.diging.vspace.core.data.FileRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IFileFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

@Service
public class FileApiManager {
    
    @Autowired
    private IStorageEngine storageEngine;
    
    @Autowired
    private IFileFactory fileFactory;
    
    @Autowired
    private FileRepository fileRepo;
    
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
                relativePath = storageEngine.storeFile(fileContent, fileName, file.getId());
            } catch (FileStorageException e) {
                returnValue.getErrorMsgs().add("File could not be stored: " + e.getMessage());
            }
            file.setParentPath(relativePath);
            file.setFileDescription(fileDescription);
            file.setOriginalFileName(originalFileName);
            fileRepo.save((VSFile) file);
        }
        return returnValue;
        
    }
    
    public List<VSFile> getAllFiles(){
        return fileRepo.findAll();
    }
    
    public IVSFile getFileById(String id) {
        Optional<VSFile> optional = fileRepo.findById(id);
        return optional.get();
    }
    
    public IVSFile editFile(String fileId, String fileName, String description) {
        IVSFile file = null;
        Optional<VSFile> optional = fileRepo.findById(fileId);
        if(!optional.isPresent()) {
            return file;
        }
        file = optional.get();
        if(storageEngine.renameFile(file, fileName)) {
            file.setFilename(fileName);   
        }
        file.setFileDescription(description);
        fileRepo.save((VSFile)file);
        return file;
    }

    public byte[] downloadFile(String fileId) throws IOException {
        IVSFile file = getFileById(fileId);

        byte[] fileContent = storageEngine.downloadFile(file.getId(), file.getFilename());
        return fileContent;
    }
    
    public void deleteFile(String fileId) {
        IVSFile file = getFileById(fileId);
        storageEngine.deleteFile(file);
    }

}
