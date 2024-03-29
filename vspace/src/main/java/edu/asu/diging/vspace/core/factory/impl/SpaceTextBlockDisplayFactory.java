package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceTextBlockDisplayFactory;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceTextBlockDisplay;

@Service
public class SpaceTextBlockDisplayFactory implements ISpaceTextBlockDisplayFactory {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.factory.impl.IModuleLinkDisplayFactory#
     * createModuleLinkDisplay(edu.asu.diging.vspace.core.model.IModuleLink)
     */
    @Override
    public ISpaceTextBlockDisplay createSpaceTextBlockDisplay(ISpaceTextBlock textBlock, float positionX, float positionY, float height, float width, 
            String textColor, String borderColor) {
        ISpaceTextBlockDisplay display = new SpaceTextBlockDisplay();
        display.setSpaceTextBlock(textBlock);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setHeight(height);
        display.setWidth(width);
        display.setTextColor(textColor);
        display.setBorderColor(borderColor);
        return display;
    }
}
