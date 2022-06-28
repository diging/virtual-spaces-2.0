package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

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
    
    public void storeDefaultImage(byte[] image, String filename);

}
