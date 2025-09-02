package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;

/**
 * Interface for managing space content blocks, extending the generic content block manager.
 */
public interface ISpaceBlockManager extends IGenericContentBlockManager<ISpaceBlock> {

    /**
     * Creates a new space block.
     * 
     * @param slideId The ID of the slide
     * @param title The space block title
     * @param space The space to be displayed in the block
     * @return The created space block
     */
    ISpaceBlock createSpaceBlock(String slideId, String title, ISpace space);

    /**
     * Saves/updates an existing space block.
     * 
     * @param spaceBlock The space block to save
     */
    void saveSpaceBlock(ISpaceBlock spaceBlock);
}
