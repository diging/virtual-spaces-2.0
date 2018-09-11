package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IDefaultExhibitionManager {

	CreationReturnValue storeSpace(DefaultExhibition exhibit);

	List<IDefaultExhibition> getAllExhibitions();

	IDefaultExhibition getExhibitionbyId(String id);

}