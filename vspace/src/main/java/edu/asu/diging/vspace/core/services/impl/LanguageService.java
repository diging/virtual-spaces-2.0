package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ILanguageService;

@Service
public class LanguageService implements ILanguageService {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Override
    public String getLocalizedText(List<ILocalizedText> localizedTexts, String languageCode, String defaultLanguageCode) {
        if (localizedTexts == null || localizedTexts.isEmpty()) {
            return "";
        }
        
        // First, try to find text in the requested language
        if (StringUtils.hasText(languageCode)) {
            for (ILocalizedText localizedText : localizedTexts) {
                if (localizedText.getExhibitionLanguage() != null && 
                    languageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // If not found, try default language
        if (StringUtils.hasText(defaultLanguageCode) && !defaultLanguageCode.equals(languageCode)) {
            for (ILocalizedText localizedText : localizedTexts) {
                if (localizedText.getExhibitionLanguage() != null && 
                    defaultLanguageCode.equals(localizedText.getExhibitionLanguage().getCode())) {
                    return localizedText.getText() != null ? localizedText.getText() : "";
                }
            }
        }
        
        // If still not found, try the exhibition's default language
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if (exhibition != null) {
            IExhibitionLanguage defaultLang = exhibitionManager.getDefaultLanguage(exhibition);
            if (defaultLang != null) {
                for (ILocalizedText localizedText : localizedTexts) {
                    if (localizedText.getExhibitionLanguage() != null && 
                        defaultLang.getCode().equals(localizedText.getExhibitionLanguage().getCode())) {
                        return localizedText.getText() != null ? localizedText.getText() : "";
                    }
                }
            }
        }
        
        // Last resort: return the first available text
        for (ILocalizedText localizedText : localizedTexts) {
            if (localizedText.getText() != null && !localizedText.getText().trim().isEmpty()) {
                return localizedText.getText();
            }
        }
        
        return "";
    }
    
    @Override
    public String getDefaultLanguageCode() {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if (exhibition != null) {
            IExhibitionLanguage defaultLang = exhibitionManager.getDefaultLanguage(exhibition);
            if (defaultLang != null) {
                return defaultLang.getCode();
            }
        }
        return "en"; // fallback to English
    }
    
    @Override
    public List<IExhibitionLanguage> getAvailableLanguages() {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if (exhibition != null && exhibition.getLanguages() != null) {
            return exhibition.getLanguages();
        }
        return List.of(); // return empty list if no languages available
    }
}
