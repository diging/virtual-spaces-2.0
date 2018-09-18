package edu.asu.diging.vspace.core.services.impl;

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

	@Autowired
	private ExhibitionRepository exhibitRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
	 * asu.diging.vspace.core.model.impl.Exhibition)
	 */
	@Override
	public IExhibition storeExhibition(Exhibition exhibit) {
		exhibit = exhibitRepo.save(exhibit);
		return exhibit;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.services.IExhibitionManager#getExhibitionById(java
	 * .lang.String)
	 */
	@Override
	public IExhibition getExhibitionById(String id) {

		Optional<Exhibition> exhibition = exhibitRepo.findById(id);
		if (exhibition.isPresent()) {
			return exhibition.get();
		}
		return null;
	}
}