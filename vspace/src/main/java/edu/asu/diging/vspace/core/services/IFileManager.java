package edu.asu.diging.vspace.core.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IFileManager {

    CreationReturnValue storeFile(byte[] fileContent, String originalFileName, String fileName, String fileDescription);

    Page<IVSFile> getAllFiles(int page);
    
    IVSFile getFileById(String id);

    IVSFile editFile(String fileId, String fileName, String description);

    Resource downloadFile(String fileName, String fileId) throws IOException;

    boolean deleteFile(String fileId);

}
