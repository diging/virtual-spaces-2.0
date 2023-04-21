package edu.asu.diging.vspace.core.file;

import java.io.IOException;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.FolderType;
import edu.asu.diging.vspace.core.model.impl.ExhibitionDownload;

public interface IStorageEngine {
    String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

    String storeFile(byte[] fileContent, String filename, String directory, String path) throws FileStorageException;

    byte[] getImageContent(String directory, String filename) throws IOException;

    boolean renameImage(IVSImage image, String newFileName);

    String createFolder(String relativePath, FolderType space);

    String createFolder(String exhibitionFolderName);

    byte[] generateZipFolder(String exhibitionFolderPath) throws IOException;

    void copyToFolder(String relativePath, String folderToCopy) throws IOException;
    
	
}