package edu.asu.diging.vspace.core.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.Resource;

import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;

public interface IStorageEngine {
    String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

    String createFolder(String relativePath);

    byte[] generateZip(String exhibitionFolderName) throws IOException;

    void copyToFolder(String relativePath, String folderToCopy) throws IOException;
    
    Resource downloadFile(String fileName, String directory) throws IOException;

    boolean renameFile(String fileName, String newFileName, String directory);

    boolean deleteFile(String fileName, String directory);

    File getFile(String fileUploadDir, String fileName);
    
    File getDirectoryPath(String directory);

    byte[] getMediaContent(String directory, String filename) throws IOException;

    boolean renameImage(IVSImage image, String newFileName);

    /**
     * Deletes the specified folder
     *
     *@param folderPath path to the folder to be deleted
     */
    void deleteFolder(String folderPath, String folderName) throws IOException;

    /**
     * To return the zip file
     * 
     * @param zipFilename - name of the folder to be zipped
     * @return byte[] - zipped data as a byte array
     * @throws ExhibitionSnapshotNotFoundException 
     * @throws IOException 
     */
    byte[] getZip(String zipFilename) throws ExhibitionSnapshotNotFoundException, IOException, FileNotFoundException;
}

