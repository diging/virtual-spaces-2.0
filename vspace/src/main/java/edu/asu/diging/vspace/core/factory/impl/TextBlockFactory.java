package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
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
    public ITextBlock createTextBlock(ISlide slide, String text) {
        ITextBlock textBlock = new TextBlock();
        textBlock.setText(text);
        textBlock.setSlide(slide);

        return textBlock;
    }

}
