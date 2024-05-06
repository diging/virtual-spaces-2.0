package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

/**
 * (non-javadoc)
 * An interface for creating an image block for a slide.
 */
public interface IImageBlockFactory {
    /**
     * (non-javadoc)
     * Creates a new image block for the specified slide and image.
     *
     * @param slide the slide to create the image block for
     * @param image the image to include in the image block
     * @return edu.asu.diging.vspace.core.model 
     */
    IImageBlock createImageBlock(ISlide slide, IVSImage image);

}
