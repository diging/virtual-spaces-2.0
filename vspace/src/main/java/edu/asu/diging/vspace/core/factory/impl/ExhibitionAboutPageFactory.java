package edu.asu.diging.vspace.core.factory.impl;

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

        aboutPageForm.getTitles().add(createLocalizedTitleForm(exhibitionAboutPage, defaultLanguage));       
        aboutPageForm.getAboutPageTexts().add(createLocalizedAboutTextForm(exhibitionAboutPage, defaultLanguage));

        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                aboutPageForm.getTitles().add(createLocalizedTitleForm(exhibitionAboutPage, language));               
                aboutPageForm.getAboutPageTexts().add(createLocalizedAboutTextForm(exhibitionAboutPage, language)); 
            }
        });
        return aboutPageForm;
    }
    
    /**
     * Creates Localized title object for form 
     * 
     * @param exhibitionAboutPage
     * @param language
     * @return
     */
    private LocalizedTextForm createLocalizedAboutTextForm(ExhibitionAboutPage exhibitionAboutPage,
            IExhibitionLanguage language) {

        LocalizedTextForm localizedAboutTextForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText aboutPageText = exhibitionAboutPage.getExhibitionTextDescriptions().stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(aboutPageText != null) {
            localizedAboutTextForm.setText(aboutPageText.getText());
            localizedAboutTextForm.setLocalisedTextId( aboutPageText.getId());

        } 
        localizedAboutTextForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedAboutTextForm;
    }
    
    
    /**
     * 
     * Creates Localized about text object for form 
     * @param exhibitionAboutPage
     * @param language
     * @return
     */
    private LocalizedTextForm createLocalizedTitleForm(ExhibitionAboutPage exhibitionAboutPage, IExhibitionLanguage language) {
        LocalizedTextForm localizedTitleForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );

        ILocalizedText title = exhibitionAboutPage.getExhibitionTitles().stream()
                .filter(exhibitionTitle ->  StringUtils.equals(exhibitionTitle.getExhibitionLanguage().getId(), language.getId())).findAny().orElse(null);

        if(title != null) {
            localizedTitleForm.setText(title.getText());
            localizedTitleForm.setLocalisedTextId(title.getId());
        } 
        localizedTitleForm.setIsDefaultExhibitionLanguage(language.isDefault());      
        
        return localizedTitleForm;
    }
}
