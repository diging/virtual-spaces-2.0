package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
/**
 * IExhibitionAboutPageManager allows to store and manage ExhibitionAboutPage.
 * @author Avirup Biswas
 *
 */
public interface IExhibitionAboutPageManager {
    
    /**
     * This method fetches all the entries from table and
     *  return a list of each entries from that table
     */
    List<ExhibitionAboutPage> findAll();
    
    /**
     * This method stores title and text of about page in  table
     *  return the current object which has been stored in table
     */
    ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage);
}
