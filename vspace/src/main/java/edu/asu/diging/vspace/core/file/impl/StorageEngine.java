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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

@Component
@PropertySource({"classpath:config.properties", "${appConfigFile:classpath:}/app.properties"})
public class StorageEngine implements IStorageEngine {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${uploads_path}")
	private String path;
	
	/* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.file.impl.IStorageEngine#storeFile(byte[], java.lang.String, java.lang.String)
     */
    @Override
    public String storeFile(byte[] fileContent, String filename, String directory, String path) throws FileStorageException {
        File parent = new File(path +   (directory!= null ? File.separator + directory : "" ));
        if (!parent.exists()) {
            parent.mkdir();
        }
        File file = new File(parent.getAbsolutePath() + File.separator + filename);
        

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
	
	
	
    
	public String createFolder(String folderName, String path ) {
	    File folder = new File(path + File.separator + folderName);
	    if (!folder.exists()) {
	        folder.mkdir();
	    }

	    return folder.getAbsolutePath();

	}
   

	/**
	 * Converts the given folder into a zip folder and returna byte array.
	 * 
	 * @param folderPath
	 * @return
	 * @throws IOException
	 */
	public byte[] generateZipFolder(String folderPath) throws IOException {
	    Path zipFile = Paths.get(folderPath);

	    try (           
	            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
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
	                System.err.println(e);
	            }
	        });
	        IOUtils.close(responseZipStream);
	        IOUtils.close(bufferedOutputStream);
	        IOUtils.close(byteArrayOutputStream);

	        return byteArrayOutputStream.toByteArray();

	    } catch (IOException e) {
	        throw new IOException(e);
	    }      

	}



	/**
	 * Copies given image to imagesFolderPath
	 * 
	 * @param image
	 * @param imagesFolderPath
	 */
	public void copyImageToFolder(IVSImage image, String imagesFolderPath) {
	    try {
	        byte[] byteArray = getImageContent(image.getId(), image.getFilename());
	        storeFile(byteArray, image.getFilename(),image.getId(), imagesFolderPath );

	    } catch (IOException | FileStorageException e) {
	        logger.error("Could not copy images" , e);
	    }     
	}
}
