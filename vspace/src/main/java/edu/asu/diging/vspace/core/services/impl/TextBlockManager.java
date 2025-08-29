package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Service
public class TextBlockManager extends AbstractContentBlockManager<ITextBlock, TextContentBlockRepository> {

    @Autowired
    private ITextBlockFactory textBlockFactory;
    
    @Autowired
    private TextContentBlockRepository textBlockRepo;

    @Override
    protected TextContentBlockRepository getRepository() {
        return textBlockRepo;
    }

    /**
     * Creates a new text block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param text The text content
     * @return The created text block
     */
    public ITextBlock createTextBlock(String slideId, String text) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        ITextBlock textBlock = textBlockFactory.createTextBlock(slide, text);
        textBlock.setContentOrder(contentOrder);
        return textBlockRepo.save((TextBlock) textBlock);
    }

    /**
     * Updates an existing text block.
     * 
     * @param textBlock The text block to update
     */
    public void updateTextBlock(ITextBlock textBlock) {
        textBlockRepo.save((TextBlock) textBlock);
    }
}
