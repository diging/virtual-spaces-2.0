package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.TextBlock;

@Service
public class TextBlockFactory implements ITextBlockFactory {

    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;

    @Override
    public ITextBlock createTextBlock(ISlide slide, String text) {
        ITextBlock textBlock = new TextBlock();
        textBlock.setText(text);
        textBlock.setSlide(slide);

        return textBlock;
    }
    
    @Override
    public ITextBlock createTextBlockWithLanguage(ISlide slide, String text, String languageCode) {
        ITextBlock textBlock = new TextBlock();
        textBlock.setText(text);
        textBlock.setSlide(slide);
        
        ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findByCode(languageCode);
        if (exhibitionLanguage != null) {
            LocalizedText localizedText = new LocalizedText(exhibitionLanguage, text);
            textBlock.getLocalizedTexts().add(localizedText);
        }

        return textBlock;
    }

}
