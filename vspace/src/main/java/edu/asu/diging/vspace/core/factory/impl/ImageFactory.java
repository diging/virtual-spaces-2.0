package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.DefaultImages;
import edu.asu.diging.vspace.core.model.impl.VSImage;

@Service
public class ImageFactory implements IImageFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageFactory#createImage(java.lang.
     * String, java.lang.String)
     */
    @Override
    public IVSImage createImage(String filename, String fileType) {
        IVSImage image = new VSImage();
        image.setFilename(filename);
        image.setFileType(fileType);
        return image;
        
        
    }
    
    @Override
    public IVSImage createDefaultImage(String filename, String fileType) {
        IVSImage defaultImage = new DefaultImages();
        defaultImage.setFilename(filename);
        defaultImage.setFileType(fileType);
        return defaultImage;
        
        
    }
    

    
}
