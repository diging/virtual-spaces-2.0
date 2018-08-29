package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.vspace.core.data.DefaultExhibitionRepository;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.services.IDefaultExhibitionManager;

public class DefaultExhibitionManager implements IDefaultExhibitionManager {

/* (non-Javadoc)
 * @see edu.asu.diging.vspace.core.services.impl.IDefaultExhibitionManager#storeSpace(edu.asu.diging.vspace.core.model.ISpace, byte[], java.lang.String)
 */
	@Autowired
	private DefaultExhibitionRepository exhibitRepo;
	
@Override
public DefaultExhibition storeSpace(DefaultExhibition exhibit) {
	return exhibitRepo.save((DefaultExhibition) exhibit);
	
}
}