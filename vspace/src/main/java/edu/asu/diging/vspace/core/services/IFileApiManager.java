package edu.asu.diging.vspace.core.services;

import java.io.IOException;
import java.util.List;

import edu.asu.diging.vspace.core.model.IVSFile;
import edu.asu.diging.vspace.core.model.impl.VSFile;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IFileApiManager {

    CreationReturnValue storeFile(byte[] fileContent, String originalFileName, String fileName, String fileDescription);

    List<VSFile> getAllFiles();

    IVSFile getFileById(String id);

    IVSFile editFile(String fileId, String fileName, String description);

    byte[] downloadFile(String fileId) throws IOException;

    boolean deleteFile(String fileId);

}
