package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;

/**
 * (non-javadoc)
 * The ISpaceTextBlockFactory interface defines a factory for creating instances of the 
 * edu.asu.diging.vspace.core.model.ISpaceTextBlock interface. 
 * Implementations of this interface should provide an implementation for the
 * method to create an space TextBlock, which takes a text and space
 * as input parameters and returns an instance of the ISpaceTextBlock interface.
 * 
 */
public interface ISpaceTextBlockFactory {
    
    /**
     * Creates a new space text block with the given text and space.
     *
     * @param text The text of the space text block.
     * @param space The space for the space text block.
     * @return The created space text instance of the edu.asu.diging.vspace.core.model.ISpaceTextBlock
     */
    ISpaceTextBlock createSpaceTextBlock(String text, ISpace space);
}
