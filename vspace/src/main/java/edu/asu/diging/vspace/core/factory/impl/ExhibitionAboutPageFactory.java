package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.factory.IExhibitionAboutPageFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

@Component
public class ExhibitionAboutPageFactory  implements IExhibitionAboutPageFactory{

    
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    /**
     * Creates About Page form object
     */
    @Override
    public AboutPageForm createAboutPageForm(ExhibitionAboutPage exhibitionAboutPage) {
        AboutPageForm aboutPageForm=new AboutPageForm();
        aboutPageForm.setAboutPageText(exhibitionAboutPage.getAboutPageText());
        aboutPageForm.setTitle(exhibitionAboutPage.getTitle());        

        IExhibition startExhibtion = exhibitionManager.getStartExhibition();    
        IExhibitionLanguage defaultLanguage = startExhibtion.getDefaultLanguage();

        aboutPageForm.setDefaultTitle(createLocalizedTextForm(exhibitionAboutPage, defaultLanguage, exhibitionAboutPage.getExhibitionTitles()));
        aboutPageForm.setDefaultAboutPageText(createLocalizedTextForm(exhibitionAboutPage, defaultLanguage, exhibitionAboutPage.getExhibitionTextDescriptions()));
        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                aboutPageForm.getTitles().add(createLocalizedTextForm(exhibitionAboutPage, language, exhibitionAboutPage.getExhibitionTitles()));               
                aboutPageForm.getAboutPageTexts().add(createLocalizedTextForm(exhibitionAboutPage, language, exhibitionAboutPage.getExhibitionTextDescriptions())); 
            }
        });
        return aboutPageForm;
    }
    
    /**
     * Creates LocalizedTextForm form using provided list of localizedTexts. 
     * @param exhibitionAboutPage
     * @param language
     * @param localizedTexts
     * @return
     */
    private LocalizedTextForm createLocalizedTextForm(ExhibitionAboutPage exhibitionAboutPage,
            IExhibitionLanguage language, List<ILocalizedText> localizedTexts) {

        LocalizedTextForm localizedAboutTextForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText aboutPageText = localizedTexts.stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(aboutPageText != null) {
            localizedAboutTextForm.setText(aboutPageText.getText());
            localizedAboutTextForm.setLocalisedTextId( aboutPageText.getId());

        } 
        localizedAboutTextForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedAboutTextForm;
    }

}
