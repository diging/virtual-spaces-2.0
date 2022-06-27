package edu.asu.diging.vspace.core.file;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;

public interface IStorageEngine {

    String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

    byte[] getImageContent(String directory, String filename) throws IOException;

    Resource downloadFile(String fileName, String directory) throws IOException;

    boolean renameFile(String fileName, String newFileName, String directory);

    boolean deleteFile(String fileName, String directory);

    File getFile(String fileUploadDir, String fileName);

    byte[] getFileContent(File fileObject) throws IOException;
    
    File getDirectoryPath(String directory);
}
