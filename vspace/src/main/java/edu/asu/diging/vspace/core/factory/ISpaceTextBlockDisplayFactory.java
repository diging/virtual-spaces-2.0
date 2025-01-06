package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

/**
 * The ISpaceTextBlockDisplayFactory interface represents a factory for creating space text block displays.
 * Implementing classes should provide a concrete implementation for creating instance of
 * edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay.
 */
public interface ISpaceTextBlockDisplayFactory {

    /**
     * Creates an ISpaceTextBlockDisplay object with the provided parameters.
     *
     * @param textBlock The ISpaceTextBlock representing the text content to be displayed.
     * @param positionX The X-coordinate position of the text block display.
     * @param positionY The Y-coordinate position of the text block display.
     * @param height    The height of the text block display.
     * @param width     The width of the text block display.
     * @param textColor The color of text in the text block display.
     * @param borderColor The border corder of the text block display.
     * @return An ISpaceTextBlockDisplay instance representing the visual display of the text block.
     */
    ISpaceTextBlockDisplay createSpaceTextBlockDisplay(ISpaceTextBlock textBlock, float positionX, float positionY, float height, float width, String textColor, String borderColor);
}
