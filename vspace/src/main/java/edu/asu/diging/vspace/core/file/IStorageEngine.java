package edu.asu.diging.vspace.core.file;

import java.io.IOException;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;

public interface IStorageEngine {

	String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

	byte[] getImageContent(String directory, String filename) throws IOException;

	boolean renameImage(IVSImage image, String newFileName);
	
	   String storeFile(byte[] fileContent, String filename, String directory, String path) throws FileStorageException;

    byte[] getFileContent(String directory, String filename, String path) throws IOException;

}