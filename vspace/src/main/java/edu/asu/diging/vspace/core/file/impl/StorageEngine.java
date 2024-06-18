package edu.asu.diging.vspace.core.file.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
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

import edu.asu.diging.vspace.core.exception.ExhibitionSnapshotNotFoundException;
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
        Path folder = Paths.get(path + File.separator + folderName);
        ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
        String zipFile = folderName+".zip";
        String folderPath = "";
        FileOutputStream fileOutputStream = new FileOutputStream(getFile(folderPath,zipFile));
        
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                ZipOutputStream responseZipStream = new ZipOutputStream(bufferedOutputStream);
                Stream<Path> paths = Files.walk(folder)) {
               for (Path path : paths.filter(p -> !Files.isDirectory(p)).collect(Collectors.toList())) {
                   ZipEntry zipEntry = new ZipEntry(folder.relativize(path).toString());
                   try {
                       responseZipStream.putNextEntry(zipEntry);
                       Files.copy(path, responseZipStream);
                       responseZipStream.closeEntry();
                   } catch (IOException e) {
                       // Delete the created zip if an exception occurs
                       deleteFile(folderPath, zipFile);
                       throw new IOException(e.getMessage(), e);
                   }
               }
               // Delete the folder
               deleteFolder(folderPath, folderName); 
           } catch (IOException e) {
               // Delete the zip file if an exception occurred
               deleteFile(folderPath, zipFile);
               deleteFolder(folderPath, folderName);
               
               throw new IOException(e.getMessage(), e);
           }
           return byteArrayOutputStream.toByteArray();        
    }
    
    /**
     * Copies the contents of a specified folder to a destination folder in the relative path.
     *
     * @param relativePath - the relative path within which the folder contents will be copied
     * @param folderToCopy - the path of the folder whose contents are to be copied
     * @throws IOException - if an I/O error occurs during the copy process
     */
    @Override
    public void copyToFolder(String relativePath, String folderToCopy) throws IOException {
        FileUtils.copyDirectory(new File(folderToCopy), new File(path + File.separator+ relativePath));
    }
    
    /**
     * Deletes the specified folder
     *
     *@param folderPath  path to the folder to be deleted
     */
    @Override
    public void deleteFolder(String folderPath, String folderName) throws IOException {
        FileUtils.deleteDirectory(getFile(folderPath,folderName));
    }
    
    /**
     * To return the zip file
     * 
     * @param zipFilename - name of the folder to be zipped
     * @return byte[] - zipped data as a byte array
     * @throws ExhibitionSnapshotNotFoundException 
     * @throws IOException 
     */
    @Override
    public byte[] getZip(String zipFilename) throws ExhibitionSnapshotNotFoundException, IOException, FileNotFoundException {
        File file = new File(path + File.separator + zipFilename + ".zip");
        if(!file.exists()){
            throw new ExhibitionSnapshotNotFoundException(zipFilename);
        }
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = in.readAllBytes();
            return buffer;
        } catch (IOException e) {
            throw new ExhibitionSnapshotNotFoundException(e.getMessage(), e);
        }                   
    }
}   
