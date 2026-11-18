package edu.asu.diging.vspace.web.staff.forms.factory;

import java.util.List;


import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

@Service
public class LocalizedTextFormFactory {

    public static final String DEFAULT_LABEL = "Default";
    
    /**
     * Creates LocalizedTextForm form using provided list of localizedTexts. 
     * @param exhibitionAboutPage
     * @param language
     * @param localizedTexts
     * @return
     */
    public LocalizedTextForm createLocalizedTextForm( IExhibitionLanguage language, List<ILocalizedText> localizedTexts) {

        LocalizedTextForm localizedAboutTextForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText localizedText = localizedTexts.stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), 
                        exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(localizedText != null) {
            localizedAboutTextForm.setText(localizedText.getText());
            localizedAboutTextForm.setLocalizedTextId( localizedText.getId());
        } 
        localizedAboutTextForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedAboutTextForm;
    }

    /**
     * Creates a LocalizedTextForm that is not linked to any exhibition language. It is used
     * when no default language is configured for the exhibition, so that the default field
     * can still be shown and filled with the plain (non-localized) text.
     * @param text the current non-localized text
     * @return form for the default field
     */
    /**
     * Creates the LocalizedTextForm for the default exhibition language. If there is no localized
     * text for the default language yet, the given non-localized text is used instead.
     * @param language the default exhibition language
     * @param localizedTexts existing localized texts
     * @param fallbackText the current non-localized text
     * @return form for the default field
     */
    public LocalizedTextForm createDefaultLanguageTextForm(IExhibitionLanguage language, List<ILocalizedText> localizedTexts, String fallbackText) {
        LocalizedTextForm localizedTextForm = createLocalizedTextForm(language, localizedTexts);
        // content created before languages were configured only has the plain (non-localized) text
        if (StringUtils.isEmpty(localizedTextForm.getText())) {
            localizedTextForm.setText(fallbackText);
        }
        return localizedTextForm;
    }

    public LocalizedTextForm createDefaultLocalizedTextForm(String text) {
        LocalizedTextForm localizedTextForm = new LocalizedTextForm(text, null, null, DEFAULT_LABEL);
        localizedTextForm.setIsDefaultExhibitionLanguage(true);
        return localizedTextForm;
    }
}