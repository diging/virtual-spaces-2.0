package edu.asu.diging.vspace.core.factory.impl;

import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

public class TextBlockFactory implements ITextBlockFactory {
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.factory.impl.ITextBlockFactory#createTextBlock(java.lang.String, java.lang.String)
     */
    @Override
    public IContentBlock createTextBlock(String slideId, String text) {
        ITextBlock txtblock = new TextBlock();
        txtblock.setText(text);
        return (IContentBlock) txtblock;
    }

}
