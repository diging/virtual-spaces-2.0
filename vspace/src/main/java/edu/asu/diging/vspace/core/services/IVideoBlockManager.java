package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.VideoCouldNotBeStoredException;
import edu.asu.diging.vspace.core.model.IVideoBlock;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

/**
 * Interface for managing video content blocks, extending the generic content block manager.
 */
public interface IVideoBlockManager extends IGenericContentBlockManager<IVideoBlock> {

    /**
     * Creates a new video block.
     * 
     * @param slideId The ID of the slide
     * @param video The video bytes (null if using URL)
     * @param size The file size
     * @param fileName The filename
     * @param url The video URL (null if using file upload)
     * @param title The video title
     * @return The creation result with the created video block
     * @throws VideoCouldNotBeStoredException if video storage fails
     */
    CreationReturnValue createVideoBlock(String slideId, byte[] video, Long size, String fileName, 
            String url, String title) throws VideoCouldNotBeStoredException;

    /**
     * Updates an existing video block.
     * 
     * @param videoBlock The video block to update
     * @param video The video bytes
     * @param fileSize The file size
     * @param url The video URL
     * @param filename The filename
     * @param title The video title
     * @throws VideoCouldNotBeStoredException if video storage fails
     */
    void updateVideoBlock(IVideoBlock videoBlock, byte[] video, Long fileSize, String url, 
            String filename, String title) throws VideoCouldNotBeStoredException;

    /**
     * Saves a video block (used for updating video properties).
     * 
     * @param videoBlock The video block to save
     */
    void saveVideoBlock(IVideoBlock videoBlock);
}
