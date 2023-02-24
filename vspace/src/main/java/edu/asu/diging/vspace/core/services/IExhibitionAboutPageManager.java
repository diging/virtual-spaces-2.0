package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
/**
 * IExhibitionAboutPageManager allows to store and manage {@link ExhibitionAboutPage}.
 * @author Avirup Biswas
 *
 */
public interface IExhibitionAboutPageManager {
    
    /**
     * This method fetches all {@link ExhibitionAboutPage} entries and returns them
     */
    List<ExhibitionAboutPage> findAll();
    
    /**
     * This method stores Exhibition About page information and returns the {@link ExhibitionAboutPage} which is being stored
     * @param {@link ExhibitionAboutPage} This object contains Exhibition about page values to be stored.
     * @return {@link ExhibitionAboutPage} Returns the object after being stored. Object will now have an ID.
     */
    ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage);

    ExhibitionAboutPage getExhibitionAboutPage();
    

    /**
     * This method stores LanguageDescriptionObject page information and returns the {@link ExhibitionAboutPage} which is being stored
     * @param {@link ExhibitionAboutPage} This object contains Exhibition about page values to be stored.
     * @param {@link AboutPageForm} This object contains Exhibition about page form values to map in ExhihibitionAboutPage lists.
     * @return {@link ExhibitionAboutPage} Returns the object after being stored. Object will now have an ID.
     */
    void storeAboutPageData(AboutPageForm aboutPageForm);

    AboutPageForm createAboutPageForm();

    LocalizedTextForm createLocalizedAboutTextForm(ExhibitionAboutPage exhibitionAboutPage, IExhibitionLanguage language) ;

    LocalizedTextForm createLocalizedTitleForm(ExhibitionAboutPage exhibitionAboutPage, IExhibitionLanguage language);


}
