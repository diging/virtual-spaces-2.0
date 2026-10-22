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
import edu.asu.diging.vspace.core.model.IVSVideo;

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
    public void copyImage(IVSImage image, String imagesFolderPath) throws FileStorageException {
        if(image!=null) {
            try {
                byte[] byteArray = storageEngineUploads.getMediaContent(image.getId(), image.getFilename());
                storageEngineDownloads.storeFile(byteArray, image.getFilename(),imagesFolderPath + File.separator+image.getId());
            } catch (IOException | FileStorageException e) {
                throw new FileStorageException(imagesFolderPath, e);
            }     
        }
    }

    /**
     * Copies given uploaded video to videosFolderPath
     * 
     * @param video
     * @param videosFolderPath
     */
    @Override
    public void copyVideo(IVSVideo video, String videosFolderPath) throws FileStorageException {
        if(video!=null && (video.getUrl()==null || video.getUrl().isEmpty()) && video.getFilename()!=null) {
            try {
                byte[] byteArray = storageEngineUploads.getMediaContent(video.getId(), video.getFilename());
                storageEngineDownloads.storeFile(byteArray, video.getFilename(), videosFolderPath + File.separator+video.getId());
            } catch (IOException | FileStorageException e) {
                throw new FileStorageException(videosFolderPath, e);
            }
        }
    }

}
