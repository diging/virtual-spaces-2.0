package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
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
     * @param {@link AboutPageForm} This object contains Exhibition about page values to be stored.
     * @return {@link ExhibitionAboutPage} Returns the object after being stored. Object will now have an ID.
     */
    ExhibitionAboutPage store(AboutPageForm aboutPageForm);

    ExhibitionAboutPage getExhibitionAboutPage();
}

