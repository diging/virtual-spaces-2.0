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
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSFile;
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
        File fileObject = getFile(directory, filename);       
        return getFileContent(fileObject);
    }
	
//    /**
//    * Method to rename image   
//    * 
//    * @param image - image file
//    * @param newFileName - new name of the file
//    * @return true if file renaming was successful, otherwise return false 
//    */ 
//    @Override
//    public boolean renameImage(IVSImage image, String newFileName) {
//        File currentFile = getFile(image.getId(), image.getFilename());
//        File renamedFile = getFile(image.getId(), newFileName);
//        return currentFile.renameTo(renamedFile);
//    }

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
        Resource resource =  new ByteArrayResource(Files.readAllBytes(path));   
        return resource;
    }

    @Override
    public byte[] getFileContent(File fileObject) throws IOException  {
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
        return storedFile.delete();
    }
    
    @Override
    public File getFile(String directory, String fileName) {

//        File parent = new File(path + File.separator + directory);
//        if (!parent.exists()) {
//            parent.mkdir();
//        }
//        return new File(parent.getAbsolutePath() + File.separator + fileName);
                return new File(path + File.separator + directory + File.separator  + fileName);
    }
}
