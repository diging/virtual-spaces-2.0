package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.SpaceDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Transactional
@Service
@PropertySource("classpath:/config.properties")
public class SpaceManager implements ISpaceManager {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private SpaceDisplayRepository spaceDisplayRepo;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    @Autowired
    private ISpaceDisplayFactory spaceDisplayFactory;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private IImageService imageService;
    
    @Autowired
    private SpaceLinkRepository spaceLinkRepo;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.ISpaceManager#storeSpace(edu.
     * asu. diging.vspace.core.model.ISpace, java.lang.String)
     */
    @Override
    public CreationReturnValue storeSpace(ISpace space, byte[] image, String filename) {
        IVSImage bgImage = null;
        List<SpaceDisplay> displays = null;
        if (space.getId() != null) {
            displays = spaceDisplayRepo.getBySpace(space);
        }
        ISpaceDisplay spaceDisplay;
        if (displays == null || displays.isEmpty()) {
            spaceDisplay = spaceDisplayFactory.createSpaceDisplay();
        } else {
            spaceDisplay = displays.get(0);
        }

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
            ImageData imageData = imageService.getImageData(image);
            if (imageData != null) {
                bgImage.setHeight(imageData.getHeight());
                bgImage.setWidth(imageData.getWidth());
            }
            imageRepo.save((VSImage) bgImage);
            space.setImage(bgImage);
            spaceDisplay.setHeight(bgImage.getHeight());
            spaceDisplay.setWidth(bgImage.getWidth());
        }

        space = spaceRepo.save((Space) space);
        spaceDisplay.setSpace(space);
        spaceDisplayRepo.save((SpaceDisplay) spaceDisplay);
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
        if (space == null) {
            return null;
        }
        // load lazy collections
        space.getSpaceLinks().size();
        space.getModuleLinks().size();
        space.getExternalLinks().size();
        return space;
    }

    @Override
    public List<ISpace> getAllSpaces() {
        List<ISpace> spaces = new ArrayList<>();
        spaceRepo.findAll().forEach(s -> spaces.add(s));
        return spaces;
    }

    /**
     * Method to delete space based on id
     * 
     * @param id
     *            if id is null throws exception, else delete corresponding
     *            space
     * @throws SpaceDoesNotExistException 
     */
    @Override
    public void deleteSpaceById(String id) throws SpaceDoesNotExistException {
        try {
            spaceRepo.deleteById(id);
        } catch (IllegalArgumentException | EmptyResultDataAccessException exception) {
            throw new SpaceDoesNotExistException(exception);
        }

    }
    
    /**
     * Method to return identifiers of target spaces of all spacelinks
     * 
     * @return list of identifiers corresponding to target spaces in each spacelink 
     */
    @Override
    public List<String> getAllTargetSpaceIds() {
        List<String> targetSpaceIdsList = new ArrayList<>();
        spaceLinkRepo.findAll().forEach(s -> {
            if (s.getTargetSpace() != null) {
                targetSpaceIdsList.add(s.getTargetSpace().getId());
            }
        });
        return targetSpaceIdsList;
    }
}
