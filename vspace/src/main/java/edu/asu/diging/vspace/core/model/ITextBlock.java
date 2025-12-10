package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ITextBlock extends IContentBlock {

    void setText(String text);

    String getText();

    void setId(String id);

    String getId();
    
    String htmlRenderedText();
    
    List<ILocalizedText> getLocalizedTexts();
    
    void setLocalizedTexts(List<ILocalizedText> localizedTexts);
    
    /**
     * Get localized text for the given language code, with fallback to default language
     * @param languageCode The language code to retrieve text for
     * @param defaultLanguageCode The default language code to fall back to
     * @return The localized text, or original text if no localization exists
     */
    String getLocalizedText(String languageCode, String defaultLanguageCode);
    
    /**
     * Get HTML-rendered localized text for the given language code
     * @param languageCode The language code to retrieve text for
     * @param defaultLanguageCode The default language code to fall back to
     * @return The HTML-rendered localized text
     */
    String getLocalizedHtmlRenderedText(String languageCode, String defaultLanguageCode);

}
