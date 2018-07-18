package edu.asu.diging.vspace.core.services.impl;

import java.time.OffsetDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class SpaceManager implements ISpaceManager {
	
	@Autowired
	private SpaceRepository spaceRepo;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.services.impl.ISpaceManager#storeSpace(edu.asu.diging.vspace.core.model.ISpace, java.lang.String)
	 */
	@Override
	public ISpace storeSpace(ISpace space, String username) {
		space.setCreatedBy(username);
		space.setCreationDate(OffsetDateTime.now());
		return spaceRepo.save((Space)space);
	}

}
