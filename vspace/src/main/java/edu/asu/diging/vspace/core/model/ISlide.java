package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISlide extends IVSpaceElement {

    void setModule(IModule module);

    IModule getModule();

    void setContents(List<IContentBlock> contents);

    List<IContentBlock> getContents();
    
    IImageBlock getFirstImageBlock();
    
    ITextBlock getFirstMatchedTextBlock(String searchTerm);
    
    List<ILocalizedText> getSlideNames();

    void setSlideNames(List<ILocalizedText> slideNames);

    void setSlideDescriptions(List<ILocalizedText> slideDescriptions);

    List<ILocalizedText> getSlideDescriptions();
    
    /**
     * Get the localized name for the slide in the specified language
     * @param languageCode The language code to get the name in
     * @param defaultLanguageCode The fallback language code if the requested language is not available
     * @return The localized name or fallback to default name
     */
    String getLocalizedName(String languageCode, String defaultLanguageCode);
    
    /**
     * Get the localized description for the slide in the specified language
     * @param languageCode The language code to get the description in
     * @param defaultLanguageCode The fallback language code if the requested language is not available
     * @return The localized description or fallback to default description
     */
    String getLocalizedDescription(String languageCode, String defaultLanguageCode);
}
