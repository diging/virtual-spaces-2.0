package edu.asu.diging.vspace.core.file.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.file.IStorageManager;
import edu.asu.diging.vspace.core.model.IVSImage;

@Service
public class StorageManager implements IStorageManager{
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    @Qualifier("storageEngineUploads")
    private IStorageEngine storageEngineUploads;
    
    @Autowired
    @Qualifier("storageEngineDownloads")
    private IStorageEngine storageEngineDownloads;
       
    /**
     * Copies given image to imagesFolderPath
     * 
     * @param image
     * @param imagesFolderPath
     */
    @Override
    public void copyImageUploadsToDownloads(IVSImage image, String imagesFolderPath) {
        if(image!=null) {
            try {
                byte[] byteArray = storageEngineUploads.getMediaContent(image.getId(), image.getFilename());
                storageEngineDownloads.storeFile(byteArray, image.getFilename(),imagesFolderPath + File.separator+image.getId());
            } catch (IOException | FileStorageException e) {
                logger.error("Could not copy images" , e);
            }     
        }
    }

}
