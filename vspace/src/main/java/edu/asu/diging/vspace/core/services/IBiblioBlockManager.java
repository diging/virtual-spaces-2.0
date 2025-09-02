package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;

/**
 * Interface for managing bibliography content blocks, extending the generic content block manager.
 */
public interface IBiblioBlockManager extends IGenericContentBlockManager<IBiblioBlock> {

    /**
     * Creates a new bibliography block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param title The bibliography title
     * @param description The bibliography description
     * @return The created bibliography block
     */
    IBiblioBlock createBiblioBlock(String slideId, String title, String description);

    /**
     * Deletes a bibliography block by ID.
     * Note: This method doesn't update content order like the other delete methods
     * because it appears to be used differently in the original implementation.
     * 
     * @param id The ID of the bibliography block to delete
     * @throws BlockDoesNotExistException if the block doesn't exist
     */
    void deleteBiblioBlockById(String id) throws BlockDoesNotExistException;

    /**
     * Updates an existing bibliography block.
     * 
     * @param biblioBlock The bibliography block to update
     */
    void updateBiblioBlock(BiblioBlock biblioBlock);
}
