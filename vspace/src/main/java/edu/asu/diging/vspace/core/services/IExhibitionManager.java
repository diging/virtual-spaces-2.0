package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IExhibitionManager {

	List<IExhibition> getAllExhibitions();
	
	CreationReturnValue storeExhibition(Exhibition exhibit);

	IExhibition getExhibitionById(String id);

}