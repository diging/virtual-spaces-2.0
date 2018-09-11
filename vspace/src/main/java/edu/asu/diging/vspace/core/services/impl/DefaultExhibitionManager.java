package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.DefaultExhibitionRepository;
import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.services.IDefaultExhibitionManager;

@Transactional
@Service
@PropertySource("classpath:/config.properties")
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

@Override
public IDefaultExhibition getExhibitionbyId(String id){
	
	Optional<DefaultExhibition> defaultExhibition = exhibitRepo.findById(id);
	if (defaultExhibition.isPresent()) {
		return defaultExhibition.get();
	}
	return null;
}

@Override
public List<IDefaultExhibition> getAllExhibitions() {
	List<IDefaultExhibition> defaultExhibition = new ArrayList<>();
	exhibitRepo.findAll().forEach(s -> defaultExhibition.add(s));
	return defaultExhibition;
	}
}