package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;

/**
 * (non-javadoc)
 * The ITextBlockFactory interface defines a factory for creating instances of the 
 * edu.asu.diging.vspace.core.model.ITextBlock interface. 
 * Implementations of this interface should provide an implementation for the
 * method to create an TextBlock, which takes a text and slide
 * as input parameters and returns an instance of the ITextBlock interface.
 * 
 */
public interface ITextBlockFactory {
    
    /**
     * Creates a new text block with the given text and slide as input.
     *
     * @param slide The slide to which the text block will be added.
     * @param text The text content of the text block.
     * @return The created text block instance of the edu.asu.diging.vspace.core.model.ITextBlock.
     */
    ITextBlock createTextBlock(ISlide slide, String text);

}
