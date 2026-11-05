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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

@Component
@PropertySource({"classpath:config.properties", "${appConfigFile:classpath:}/app.properties"})
public class StorageEngine implements IStorageEngine {

    @Value("${uploads_path}")
    private String path;


    /* (non-Javadoc)
    * @see edu.asu.diging.vspace.core.file.impl.IStorageEngine#storeFile(byte[], java.lang.String, java.lang.String)
    */
    @Override
    public String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException {        
        File file;
        try {
            file = getFile(directory, filename);
        } catch (IllegalArgumentException e) {
            throw new FileStorageException("Could not store file.", e);
        }
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
        File currentFile;
        File renamedFile;
        try {
            currentFile = getFile(directory, fileName);
            renamedFile = getFile(directory, newFileName);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return currentFile.renameTo(renamedFile);
    }

    @Override
    public Resource downloadFile(String fileName, String directory) throws IOException {               
        File fileObject;
        try {
            fileObject = getFile(directory, fileName);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid file path.", e);
        }
        return new ByteArrayResource(Files.readAllBytes(fileObject.toPath()));   
    }

    @Override
    public byte[] getMediaContent(String directory, String filename) throws IOException {
        File fileObject;
        try {
            fileObject = resolve(directory, filename);
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid file path.", e);
        }
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
        File storedFile;
        File storedDirectory;
        try {
            storedFile = getFile(directory,fileName);               
            storedDirectory = getDirectoryPath(directory);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return storedFile.delete() && storedDirectory.delete() ;

    }
    
    @Override
    public File getFile(String directory, String fileName) {
        File parentDirectory = getDirectoryPath(directory);
        File file = resolve(directory, fileName);
        if (!parentDirectory.exists()) {
            parentDirectory.mkdir();
        }
        return file;
    }
    
    @Override
    public File getDirectoryPath(String directory) {
        checkPathSegment(directory);
        Path baseDirectory = Paths.get(path).toAbsolutePath().normalize();
        Path directoryPath = baseDirectory.resolve(directory).normalize();
        if (!baseDirectory.equals(directoryPath.getParent())) {
            throw new IllegalArgumentException("Invalid directory: " + directory);
        }
        return directoryPath.toFile();
    }
    
    /**
     * Resolves a file inside the uploads folder and makes sure it cannot
     * point anywhere other than directly inside the given directory.
     *
     * @throws IllegalArgumentException if the directory or file name is not a single, safe path segment
     */
    private File resolve(String directory, String fileName) {
        checkPathSegment(fileName);
        Path directoryPath = getDirectoryPath(directory).toPath();
        Path filePath = directoryPath.resolve(fileName).normalize();
        if (!directoryPath.equals(filePath.getParent())) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
        return filePath.toFile();
    }
    
    private void checkPathSegment(String segment) {
        if (segment == null || segment.trim().isEmpty() || segment.equals(".") || segment.equals("..")
                || segment.contains("/") || segment.contains("\\") || segment.contains("\0")) {
            throw new IllegalArgumentException("Invalid path segment: " + segment);
        }
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
        return renameFile(image.getFilename(), newFileName, image.getId());
    }
}
