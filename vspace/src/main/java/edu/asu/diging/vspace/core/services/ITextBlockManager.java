package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

public interface ITextBlockManager extends IGenericContentBlockManager<ITextBlock> {

    ITextBlock createTextBlock(String slideId, String text);

    void updateTextBlock(TextBlock textBlock);
}
