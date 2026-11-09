package edu.asu.diging.vspace.core.file;

import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSVideo;

public interface IStorageManager {

    /**
     * Copies given image from uploads to exhibition snapshot folder
     * 
     * @param image
     * @param imagesFolderPath
     * @throws FileStorageException 
     */
    void copyImage(IVSImage image, String imagesFolderPath) throws FileStorageException;

    /**
     * Copies given uploaded video from uploads to exhibition snapshot folder.
     * Videos that are only referenced by a URL are not copied.
     * 
     * @param video
     * @param videosFolderPath
     * @throws FileStorageException 
     */
    void copyVideo(IVSVideo video, String videosFolderPath) throws FileStorageException;

}
