package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

/*
 * (non-Javadoc)
 * 
 * IExhibitionManager allows to store and manage Exhibition.
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

}
