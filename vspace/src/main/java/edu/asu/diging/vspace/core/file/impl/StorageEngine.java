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
    public byte[] getImageContent(String directory, String filename) throws IOException {
        return getMediaContent(directory, filename);
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
}
