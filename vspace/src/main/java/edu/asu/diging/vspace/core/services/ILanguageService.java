package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;

/**
 * Service to handle language selection and localized text retrieval
 */
public interface ILanguageService {
    
    /**
     * Gets localized text for the specified language from a list of localized texts
     * @param localizedTexts List of localized texts
     * @param languageCode Language code to find (e.g., "en", "es")
     * @param defaultLanguageCode Fallback language code if primary not found
     * @return The text in the requested language, or default language, or first available
     */
    String getLocalizedText(List<ILocalizedText> localizedTexts, String languageCode, String defaultLanguageCode);
    
    /**
     * Gets the default exhibition language code
     * @return Default language code
     */
    String getDefaultLanguageCode();
    
    /**
     * Gets all available exhibition languages
     * @return List of exhibition languages
     */
    List<IExhibitionLanguage> getAvailableLanguages();
}
