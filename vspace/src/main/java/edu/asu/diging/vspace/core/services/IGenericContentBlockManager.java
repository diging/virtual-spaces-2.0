package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IContentBlock;

/**
 * Generic interface for content block managers following the pattern of ILinkManager.
 * 
 * @param <T> The type of content block this manager handles
 */
public interface IGenericContentBlockManager<T extends IContentBlock> {

    /**
     * Creates a new content block.
     * 
     * @param slideId The ID of the slide
     * @return The created content block
     */
    T createContentBlock(String slideId);

    /**
     * Updates an existing content block.
     * 
     * @param contentBlock The content block to update
     */
    void updateContentBlock(T contentBlock);

    /**
     * Retrieves a content block by ID.
     * 
     * @param blockId The ID of the block to retrieve
     * @return The content block or null if not found
     */
    T getContentBlock(String blockId);

    /**
     * Deletes a content block by ID and updates content order.
     * 
     * @param blockId The ID of the block to delete
     * @param slideId The ID of the slide containing the block
     * @throws BlockDoesNotExistException if the block doesn't exist
     */
    void deleteContentBlock(String blockId, String slideId) throws BlockDoesNotExistException;

    /**
     * Saves a content block.
     * 
     * @param contentBlock The content block to save
     */
    void saveContentBlock(T contentBlock);
}
