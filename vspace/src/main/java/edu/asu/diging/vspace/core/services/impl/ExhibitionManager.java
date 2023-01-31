package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Transactional
@Service
public class ExhibitionManager implements IExhibitionManager {

	@Autowired
	private ExhibitionRepository exhibitRepo;

	@Autowired
	private IImageFactory imageFactory;

	@Autowired
	private IImageService imageService;

	@Autowired
	private ImageRepository imageRepo;
	
	

	@Autowired
	private IStorageEngine storage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.services.IExhibitionManager#storeExhibition(edu.
	 * asu.diging.vspace.core.model.impl.Exhibition)
	 */
	@Override
	public IExhibition storeExhibition(Exhibition exhibition) {
		return exhibitRepo.save(exhibition);
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

	@Override
	public List<IExhibition> findAll() {
		Iterable<Exhibition> exhibitions = exhibitRepo.findAll();
		List<IExhibition> results = new ArrayList<>();
		exhibitions.forEach(e -> results.add((IExhibition) e));
		return results;
	}

	@Override
	public IExhibition getStartExhibition() {
		// for now we just take the first one created, there shouldn't be more than one
		List<Exhibition> exhibitions = exhibitRepo.findAllByOrderByIdAsc();
		if (exhibitions.size() > 0) {
			return exhibitions.get(0);
		}
		return null;
	}

	@Override
	public void storeDefaultImage(byte[] image, String filename, String id) {
	    
		IVSImage defaultImage = null;
		if (image != null && image.length > 0) {
			Tika tika = new Tika();
			String contentType = tika.detect(image);
			defaultImage = imageFactory.createDefaultImage(filename, contentType, id);
			defaultImage = imageRepo.save((VSImage) defaultImage);
		}

		CreationReturnValue returnValue = new CreationReturnValue();
		returnValue.setErrorMsgs(new ArrayList<>());

		if (defaultImage != null) {
			String relativePath = null;
			try {
				relativePath = storage.storeFile(image, filename, defaultImage.getId());
			} catch (FileStorageException e) {
				returnValue.getErrorMsgs().add("Default image could not be stored: " + e.getMessage());
			}
			defaultImage.setParentPath(relativePath);
			ImageData imageData = imageService.getImageData(image);
			if (imageData != null) {
				defaultImage.setHeight(imageData.getHeight());
				defaultImage.setWidth(imageData.getWidth());
			}
			imageRepo.save((VSImage) defaultImage);

		}

	}

}
