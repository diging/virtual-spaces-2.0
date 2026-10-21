package edu.asu.diging.vspace.web.staff.forms.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.factory.IAboutPageFormFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;

@Component
public class AboutPageFormFactory  implements IAboutPageFormFactory{

    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private LocalizedTextFormFactory localizedTextFormCreation;
    
    /**
     * Creates About Page form object
     */
    @Override
    public AboutPageForm createAboutPageForm(ExhibitionAboutPage exhibitionAboutPage) {
        AboutPageForm aboutPageForm=new AboutPageForm();
        aboutPageForm.setAboutPageText(exhibitionAboutPage.getAboutPageText());
        aboutPageForm.setTitle(exhibitionAboutPage.getTitle());        

        IExhibition startExhibition = exhibitionManager.getStartExhibition();    
        IExhibitionLanguage defaultLanguage = exhibitionManager.getDefaultLanguage(startExhibition);

        aboutPageForm.setDefaultTitle(localizedTextFormCreation.createLocalizedTextForm( defaultLanguage, 
                exhibitionAboutPage.getExhibitionTitles()));
        aboutPageForm.setDefaultAboutPageText(localizedTextFormCreation.createLocalizedTextForm( defaultLanguage, 
                exhibitionAboutPage.getExhibitionTextDescriptions()));
        startExhibition.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                aboutPageForm.getTitles().add(localizedTextFormCreation.createLocalizedTextForm( language, 
                        exhibitionAboutPage.getExhibitionTitles()));               
                aboutPageForm.getAboutPageTexts().add(localizedTextFormCreation.createLocalizedTextForm(language, 
                        exhibitionAboutPage.getExhibitionTextDescriptions())); 
            }
        });
        return aboutPageForm;
    }
}