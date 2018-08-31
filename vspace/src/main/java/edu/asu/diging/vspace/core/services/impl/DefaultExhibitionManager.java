package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.DefaultExhibitionRepository;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.services.IDefaultExhibitionManager;

@Service
public class DefaultExhibitionManager implements IDefaultExhibitionManager {

/* (non-Javadoc)
 * @see edu.asu.diging.vspace.core.services.impl.IDefaultExhibitionManager#storeSpace(edu.asu.diging.vspace.core.model.ISpace, byte[], java.lang.String)
 */
	@Autowired
	private DefaultExhibitionRepository exhibitRepo;
	
@Override
public CreationReturnValue storeSpace(DefaultExhibition exhibit) {

	CreationReturnValue returnValue = new CreationReturnValue();
	returnValue.setErrorMsgs(new ArrayList<>());
	exhibit = exhibitRepo.save(exhibit);
	returnValue.setElement(exhibit);
	return returnValue;
	
}
}