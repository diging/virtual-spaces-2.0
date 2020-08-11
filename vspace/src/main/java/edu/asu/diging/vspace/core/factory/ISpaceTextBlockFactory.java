package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceTextBlock;

public interface ISpaceTextBlockFactory {
    ISpaceTextBlock createSpaceTextBlock(String text, ISpace space);
}
