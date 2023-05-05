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

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;

public class StorageEngine  implements IStorageEngine {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private String path;

    public StorageEngine() {
        super();
    }

    public StorageEngine(String path) {
        this.path = path;
    }

    @Override
    public String storeFile(byte[] fileContent, String filename, String directory) throws FileStorageException {
        File parent = new File(path + File.separator + directory);
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

    @Override
    public boolean renameImage(IVSImage image, String newFileName) {
        File currentFile = new File(path + File.separator + image.getId() + File.separator + image.getFilename());
        File renamedFile = new File(path + File.separator + image.getId() + File.separator + newFileName);
        return currentFile.renameTo(renamedFile);
    }

    @Override
    public String createFolder(String relativePath) {
        File folder = new File(path + File.separator + relativePath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return relativePath;
    }

    @Override
    public byte[] generateZipFolder(String folderPath) throws IOException {
        Path zipFile = Paths.get(path + File.separator + folderPath);
        ByteArrayOutputStream byteArrayOutputStreamResult = null;

        try (ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream();
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
                        logger.error("Could not generate Zip folder");
                    }
                });
            byteArrayOutputStreamResult = byteArrayOutputStream;
        } catch (IOException e) {
            throw new IOException(e);
        }   
        return byteArrayOutputStreamResult.toByteArray();
    }

    @Override
    public void copyToFolder(String relativePath, String folderToCopy) throws IOException {
        try {
            FileUtils.copyDirectory(new File(folderToCopy), new File(path + File.separator+ relativePath)); 
        } catch (IOException e) {
            logger.error("Could not copy resources" , e);
            throw new IOException(e);

        }       
    }


}
