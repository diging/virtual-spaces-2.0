package edu.asu.diging.vspace.core.file;

import java.io.IOException;

import edu.asu.diging.vspace.core.exception.FileStorageException;

public interface IStorageEngine {

	String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException;

	byte[] getImageContent(String directory, String filename) throws IOException;

	boolean renameFile(String directory, String currentFileName, String newFileName);
}