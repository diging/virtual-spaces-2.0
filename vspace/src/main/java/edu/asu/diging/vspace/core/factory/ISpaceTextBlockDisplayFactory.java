package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpaceTextBlock;
import edu.asu.diging.vspace.core.model.display.ISpaceTextBlockDisplay;

public interface ISpaceTextBlockDisplayFactory {
    ISpaceTextBlockDisplay createSpaceTextBlockDisplay(ISpaceTextBlock textBlock);
}
