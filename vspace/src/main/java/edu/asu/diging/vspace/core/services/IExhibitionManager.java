package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

/**
 * 
 * @see edu.asu.diging.vspace.core.services.IExhibitionManager
 *
 */
public interface IExhibitionManager {

  /**
   * @see edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
   * asu.diging.vspace.core.model.impl.Exhibition)
   */
  IExhibition storeExhibition(Exhibition exhibit);

  /**
   * @see edu.asu.diging.vspace.core.services.IExhibitionManager#getExhibitionById(java
   * .lang.String)
   */
  IExhibition getExhibitionById(String id);

}
