package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

/**
 * Interface for managing image content blocks, extending the generic content block manager.
 */
public interface IImageBlockManager extends IGenericContentBlockManager<IImageBlock> {

    /**
     * Creates a new image block with uploaded image data.
     * 
     * @param slideId The ID of the slide
     * @param image The image bytes
     * @param filename The filename
     * @return The creation result with the created image block
     * @throws ImageCouldNotBeStoredException if image storage fails
     */
    CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException;

    /**
     * Creates a new image block with an existing image.
     * 
     * @param slideId The ID of the slide
     * @param image The existing image
     * @return The creation result with the created image block
     */
    CreationReturnValue createImageBlock(String slideId, IVSImage image);

    /**
     * Updates an image block with new image data.
     * 
     * @param imageBlock The image block to update
     * @param image The new image bytes
     * @param filename The filename
     * @throws ImageCouldNotBeStoredException if image storage fails
     */
    void updateImageBlock(IImageBlock imageBlock, byte[] image, String filename) 
            throws ImageCouldNotBeStoredException;

    /**
     * Updates an image block with an existing image.
     * 
     * @param imageBlock The image block to update
     * @param image The existing image
     */
    void updateImageBlock(IImageBlock imageBlock, IVSImage image);
}
