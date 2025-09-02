package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

/**
 * Interface for managing text content blocks, extending the generic content block manager.
 */
public interface ITextBlockManager extends IGenericContentBlockManager<ITextBlock> {

    /**
     * Creates a new text block with content.
     * 
     * @param slideId The ID of the slide
     * @param text The text content
     * @return The created text block
     */
    ITextBlock createTextBlock(String slideId, String text);

    /**
     * Updates an existing text block.
     * 
     * @param textBlock The text block to update
     */
    void updateTextBlock(TextBlock textBlock);
}
