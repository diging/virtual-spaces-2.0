package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
@PropertySource("classpath:/config.properties")
public class SpaceManager implements ISpaceManager {

	@Autowired
	private SpaceRepository spaceRepo;

	@Autowired
	private ImageRepository imageRepo;
	
	@Autowired
	private SpaceLinkRepository spaceLinkRepo;
	
	@Autowired
	private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

	@Autowired
	private IStorageEngine storage;

	@Autowired
	private IImageFactory imageFactory;
	
	@Autowired
	private ISpaceLinkFactory spaceLinkFactory;
	
	@Autowired
	private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.services.impl.ISpaceManager#storeSpace(edu.asu.
	 * diging.vspace.core.model.ISpace, java.lang.String)
	 */
	@Override
	public CreationReturnValue storeSpace(ISpace space, byte[] image, String filename) {
		IVSImage bgImage = null;
		if (image != null && image.length > 0) {
			Tika tika = new Tika();
			String contentType = tika.detect(image);

			bgImage = imageFactory.createImage(filename, contentType);
			bgImage = imageRepo.save((VSImage) bgImage);
		}

		CreationReturnValue returnValue = new CreationReturnValue();
		returnValue.setErrorMsgs(new ArrayList<>());
		
		if (bgImage != null) {
			String relativePath = null;
			try {
				relativePath = storage.storeFile(image, filename, bgImage.getId());
			} catch (FileStorageException e) {
				returnValue.getErrorMsgs().add("Background image could not be stored: " + e.getMessage());
			}
			bgImage.setParentPath(relativePath);
			imageRepo.save((VSImage) bgImage);
			space.setImage(bgImage);
		}

		space = spaceRepo.save((Space) space);
		returnValue.setElement(space);
		return returnValue;
	}

	@Override
	public ISpace getSpace(String id) {
		Optional<Space> space = spaceRepo.findById(id);
		if (space.isPresent()) {
			return space.get();
		}
		return null;
	}
	
	@Override
	public ISpace getFullyLoadedSpace(String id) {
		ISpace space = getSpace(id);
		// load lazy loaded collections
		space.getSpaceLinks().size();
		space.getModuleLinks().size();
		return space;
	}
	
	@Override
	public List<ISpaceLinkDisplay> getSpaceLinkDisplays(String spaceId) {
		return new ArrayList<>(spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(spaceId));
	}
	
	@Override
	public ISpaceLinkDisplay createSpaceLink(String title, ISpace source, float positionX, float positionY, int rotation, String linkedSpaceId, String spaceLinkLabel) throws SpaceDoesNotExistException {
		// we need this to fully load the space
		source = spaceRepo.findById(source.getId()).get();
		ISpace target = spaceRepo.findById(linkedSpaceId).get();
		if (target == null) {
			throw new SpaceDoesNotExistException();
		}
		ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
		link.setTargetSpace(target);
		spaceLinkRepo.save((SpaceLink) link);
		
		ISpaceLinkDisplay display = spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
		display.setPositionX(positionX);
		display.setPositionY(positionY);
		display.setRotation(rotation);
		display.setSpaceLinkLable(spaceLinkLabel);
		spaceLinkDisplayRepo.save((SpaceLinkDisplay)display);
		return display;
	}
	
	@Override
	public List<ISpace> getAllSpaces() {
		List<ISpace> spaces = new ArrayList<>();
		spaceRepo.findAll().forEach(s -> spaces.add(s));
		return spaces;
	}
}
