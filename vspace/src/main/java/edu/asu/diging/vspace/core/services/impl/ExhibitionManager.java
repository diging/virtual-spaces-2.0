package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Transactional
@Service
@PropertySource("classpath:/config.properties")
public class ExhibitionManager implements IExhibitionManager {

/* (non-Javadoc)
 * @see edu.asu.diging.vspace.core.services.impl.IDefaultExhibitionManager#storeSpace(edu.asu.diging.vspace.core.model.ISpace, byte[], java.lang.String)
 */
	@Autowired
	private ExhibitionRepository exhibitRepo;
	
@Override
public CreationReturnValue storeExhibition(Exhibition exhibit) {

	CreationReturnValue returnValue = new CreationReturnValue();
	returnValue.setErrorMsgs(new ArrayList<>());
	exhibit = exhibitRepo.save(exhibit);
	returnValue.setElement(exhibit);
	return returnValue;
	
}

@Override
public IExhibition getExhibitionById(String id){
	
	Optional<Exhibition> defaultExhibition = exhibitRepo.findById(id);
	if (defaultExhibition.isPresent()) {
		return defaultExhibition.get();
	}
	return null;
}

@Override
public List<IExhibition> getAllExhibitions() {
	List<IExhibition> defaultExhibition = new ArrayList<>();
	exhibitRepo.findAll().forEach(s -> defaultExhibition.add(s));
	return defaultExhibition;
	}
}