package edu.asu.diging.vspace.web.staff.forms.factory;

import java.util.List;


import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

@Service
public class LocalizedTextFormFactory {
    
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
}