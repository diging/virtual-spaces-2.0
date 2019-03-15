package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.impl.SlideManager;

@Service
public class TextBlockFactory implements ITextBlockFactory {
    
    @Autowired
    private SlideManager slideManager;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.factory.impl.ITextBlockFactory#createTextBlock(java.lang.String, java.lang.String)
     */
    @Override
    public IContentBlock createTextBlock(String slideId, String text) {
        IContentBlock txtblock = new TextBlock();
        ((ITextBlock)txtblock).setText(text);
        txtblock.setSlide(slideManager.getSlide(slideId));
        System.out.println("inside facorty");
        return (IContentBlock) txtblock;
    }

}
