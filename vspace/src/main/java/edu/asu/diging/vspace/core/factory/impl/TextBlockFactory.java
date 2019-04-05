package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Service
public class TextBlockFactory implements ITextBlockFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ITextBlockFactory#createTextBlock(
     * java.lang.String, java.lang.String)
     */
    @Override
    public IContentBlock createTextBlock(ISlide slide, String text) {
        IContentBlock textblock = new TextBlock();
        ((ITextBlock) textblock).setText(text);
        textblock.setDescription("text");
        textblock.setSlide(slide);

        return (IContentBlock) textblock;
    }

}
