package edu.asu.diging.vspace.core.file;

import edu.asu.diging.vspace.core.model.IVSImage;

public interface IStorageManager {

    /**
     * Copies given image from uploads to exhibition snapshot folder
     * 
     * @param image
     * @param imagesFolderPath
     */
    void copyImage(IVSImage image, String imagesFolderPath);

}
