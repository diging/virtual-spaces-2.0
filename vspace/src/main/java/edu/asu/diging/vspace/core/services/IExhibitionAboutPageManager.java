package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
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
     * @param {@link ExhibitionAboutPage} This object contain the value of Exhibition About page information
     * @return {@link ExhibitionAboutPage} Return the object after being stored along with auto generated Id attached to it.
     */
    ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage);
}
