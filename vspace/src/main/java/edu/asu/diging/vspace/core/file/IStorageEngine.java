package edu.asu.diging.vspace.core.file;

import java.io.IOException;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;

public interface IStorageEngine {
    String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

    String createFolder(String relativePath);

    byte[] generateZipFolder(String exhibitionFolderPath) throws IOException;

    void copyToFolder(String relativePath, String folderToCopy) throws IOException;
    
    byte[] getMediaContent(String directory, String filename) throws IOException;

    boolean renameImage(IVSImage image, String newFileName);
}