package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.ExhibitionLanguageCouldNotBeDeletedException;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

/*
 * (non-Javadoc)
 * 
 * IExhibitionManager allows to store and manage Exhibition.
 * 
 * @see edu.asu.diging.vspace.core.model.IExhibition
 */
public interface IExhibitionManager {

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
     * asu.diging.vspace.core.model.impl.Exhibition)
     */
    IExhibition storeExhibition(Exhibition exhibit);

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.vspace.core.services.IExhibitionManager#getExhibitionById(java
     * .lang.String)
     */
    IExhibition getExhibitionById(String id);

    List<IExhibition> findAll();

    IExhibition getStartExhibition();

    void updateExhibitionLanguages(Exhibition exhibition, List<String> languages, String defaultLanguage) throws ExhibitionLanguageCouldNotBeDeletedException;
    
    boolean localizedTextDoesNotExist(IExhibitionLanguage language);

}
