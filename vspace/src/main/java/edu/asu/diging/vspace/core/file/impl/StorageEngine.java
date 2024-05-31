package edu.asu.diging.vspace.core.file.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

public class StorageEngine implements IStorageEngine {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String path;
    
    public StorageEngine(String path) {
        this.path = path;
    }

    /* (non-Javadoc)
    * @see edu.asu.diging.vspace.core.file.impl.IStorageEngine#storeFile(byte[], java.lang.String, java.lang.String)
    */
    @Override
    public String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException {        
        File file = getFile(directory, filename);
        BufferedOutputStream stream;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new FileStorageException("Could not store file.", e);
        }
        try {
            stream.write(fileContent);
            stream.close();
        } catch (IOException e) {
            throw new FileStorageException("Could not store file.", e);
        }

        return directory;
    }

    @Override
    public boolean renameFile(String fileName, String newFileName, String directory) {             
        File currentFile = getFile(directory, fileName);
        File renamedFile = getFile(directory, newFileName);
        return currentFile.renameTo(renamedFile);
    }

    @Override
    public Resource downloadFile(String fileName, String directory) throws IOException {               
        File fileObject = getFile(directory, fileName);
        Path path = Paths.get(fileObject.getAbsolutePath());
        return new ByteArrayResource(Files.readAllBytes(path));   
    }

    @Override
    public byte[] getMediaContent(String directory, String filename) throws IOException {
        File fileObject = new File(path + File.separator + directory + File.separator + filename);
        URLConnection con = fileObject.toURI().toURL().openConnection();

        InputStream input = con.getInputStream();

        byte[] buffer = new byte[4096];

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        BufferedOutputStream output = new BufferedOutputStream(byteOutput);

        int n = -1;
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        input.close();
        output.flush();
        output.close();

        byteOutput.flush();
        byte[] bytes = byteOutput.toByteArray();
        byteOutput.close();
        return bytes;
    }

    @Override
    public boolean deleteFile(String fileName, String directory) {        
        File storedFile = getFile(directory,fileName);               
        File storedDirectory = getDirectoryPath(directory);
        return storedFile.delete() && storedDirectory.delete() ;

    }
    
    @Override
    public File getFile(String directory, String fileName) {
        File parentDirectory = getDirectoryPath(directory);
        if (!parentDirectory.exists()) {
            parentDirectory.mkdir();
        }
        return new File(parentDirectory.getAbsolutePath() + File.separator + fileName);
    }
    
    @Override
    public File getDirectoryPath(String directory) {
        return new File(path + File.separator + directory);
    }
    
    
    /**
     * Method to rename image   
     * 
     * @param image - image file
     * @param newFileName - new name of the file
     * @return true if file renaming was successful, otherwise return false 
     */ 
    @Override
    public boolean renameImage(IVSImage image, String newFileName) {
        File currentFile = new File(path + File.separator + image.getId() + File.separator + image.getFilename());
        File renamedFile = new File(path + File.separator + image.getId() + File.separator + newFileName);
        return currentFile.renameTo(renamedFile);
    }

    /**
     * To create a folder with given name, if it doesn't exist
     * 
     * @param folderName - folder name to be created
     * @return folderName 
     */
    @Override
    public String createFolder(String folderName) {
        File folder = new File(path + File.separator + folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folderName;
    }

    
    /**
     * To create a zip of a given folder
     * 
     * @param folderName - name of the folder to be zipped
     * @return byte[] - zipped data as a byte array
     * @throws IOException if an I/O error occurs while reading files or writing the zip data
     */
    @Override
    public byte[] generateZip(String folderName) throws IOException {
        Path zipFile = Paths.get(path + File.separator + folderName);
        ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
        
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                ZipOutputStream responseZipStream = new ZipOutputStream(bufferedOutputStream);
                Stream<Path> paths = Files.walk(zipFile)) {
            paths
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry  zipEntry = new ZipEntry(zipFile.relativize(path).toString());
                    try {
                        responseZipStream.putNextEntry(zipEntry);
                        Files.copy(path, responseZipStream);
                        responseZipStream.closeEntry();
                    } catch (IOException e) {
                        logger.error("Could not generate Zip folder", e);
                    }
                });            
        }
        return byteArrayOutputStream.toByteArray();
        
    }
    
    /**
     * Gets the list of directories 
     *  
     * @param relativePath
     * @param folderToCopy
     * @throws IOException
     */
    @Override
    public void copyToFolder(String relativePath, String folderToCopy) throws IOException {
        FileUtils.copyDirectory(new File(folderToCopy), new File(path + File.separator+ relativePath));
    }
}
