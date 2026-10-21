package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.TextBlock;
import edu.asu.diging.vspace.core.services.ITextBlockManager;

@Service
public class TextBlockManager extends GenericContentBlockManager<ITextBlock, TextContentBlockRepository> 
        implements ITextBlockManager {

    @Autowired
    private ITextBlockFactory textBlockFactory;
    
    @Autowired
    private TextContentBlockRepository textBlockRepo;

    @Override
    protected TextContentBlockRepository getRepository() {
        return textBlockRepo;
    }

    @Override
    public ITextBlock createContentBlock(String slideId) {
        return createTextBlock(slideId, "");
    }

    @Override
    public ITextBlock createTextBlock(String slideId, String text) {
        ISlide slide = slideManager.getSlide(slideId);
        Integer contentOrder = getNextContentOrder(slideId);
        
        ITextBlock textBlock = textBlockFactory.createTextBlock(slide, text);
        textBlock.setContentOrder(contentOrder);
        return textBlockRepo.save((TextBlock) textBlock);
    }

    @Override
    public void updateContentBlock(ITextBlock textBlock) {
        updateTextBlock((TextBlock) textBlock);
    }

    @Override
    public void updateTextBlock(TextBlock textBlock) {
        textBlockRepo.save(textBlock);
    }

    @Override
    public void saveContentBlock(ITextBlock textBlock) {
        textBlockRepo.save((TextBlock) textBlock);
    }
}
