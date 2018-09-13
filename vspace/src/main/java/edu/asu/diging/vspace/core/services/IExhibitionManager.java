package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;

public interface IExhibitionManager {

	IExhibition storeExhibition(Exhibition exhibit);

	IExhibition getExhibitionById(String id);

}