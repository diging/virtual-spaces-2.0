package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

public interface IDefaultExhibitionManager {

	DefaultExhibition storeSpace(DefaultExhibition exhibit);

}