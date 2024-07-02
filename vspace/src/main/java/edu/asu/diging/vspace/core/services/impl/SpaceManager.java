package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.SpaceDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
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

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ExhibitionRepository exhibitRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.ISpaceManager#storeSpace(edu.asu.
     * diging.vspace.core.model.ISpace,java.util.Arrays, java.lang.String)
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.ISpaceManager#storeSpace(edu.asu.
     * diging.vspace.core.model.ISpace,edu.asu.diging.vspace.core.model.impl.
     * VSImage)
     */
    @Override
    public CreationReturnValue storeSpace(ISpace space, IVSImage image) {
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

        if (image != null) {
            space.setImage(image);
            spaceDisplay.setHeight(image.getHeight());
            spaceDisplay.setWidth(image.getWidth());
        }

        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());

        space = spaceRepo.save((Space) space);
        spaceDisplay.setSpace(space);
        spaceDisplayRepo.save((SpaceDisplay) spaceDisplay);
        returnValue.setElement(space);
        return returnValue;
    }
    
    @Override
    public List<ISpace> findByName(String searchText){
        String searchTerm = "%" + searchText + "%";
        List<Space> spaces = spaceRepo.findByNameLike(searchTerm);
        List<ISpace> spaceResults = new ArrayList<>();
        spaces.forEach(r -> spaceResults.add(r));
        return spaceResults;
    }

    @Override
    public ISpace getSpace(String id) {
        Optional<Space> space = spaceRepo.findById(id);
        if (space != null && space.isPresent()) {
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

    @Override
    public List<ISpace> getSpacesWithStatus(SpaceStatus status) {
        List<ISpace> spaces = new ArrayList<>();
        spaceRepo.findAllBySpaceStatus(status).forEach(s -> spaces.add(s));
        return spaces;
    }

    /**
     * Method to delete space based on id
     * 
     * @param id
     *            if id is null throws exception, else delete corresponding space
     * @throws SpaceDoesNotExistException
     */
    @Override
    public void deleteSpaceById(String id) {
        if (id != null) {
            List<SpaceLink> spaceLinks = spaceLinkRepo.getLinkedSpaces(id);
            List<SpaceLink> fromSpaceLinks = new ArrayList<>();
            Optional<Space> space = spaceRepo.findById(id);
            if (space.isPresent()) {
                fromSpaceLinks = spaceLinkRepo.findByTargetSpace(space.get());
            } 
            Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
            // When space has other links attached to it
            // To delete links that access to the space getting deleted and replacing it as
            // null
            for (SpaceLink spaceLink : fromSpaceLinks) {
                spaceLink.setTargetSpace(null);
                spaceLinkRepo.save(spaceLink);
            }
            // To delete the links on the space getting deleted
            for (SpaceLink spaceLink : spaceLinks) {
                spaceLinkDisplayRepo.deleteBySpaceLinkId(spaceLink.getId());
            }
            spaceLinkRepo.deleteBySourceSpaceId(id);
            // If the space is startSpace, we delete the space from the exhibition first.
            if (exhibition != null && exhibition.getStartSpace() != null
                    && exhibition.getStartSpace().getId().equalsIgnoreCase(id)) {
                exhibition.setStartSpace(null);
                exhibitRepo.save(exhibition);
            }
            // When space has no other links attached to it
            spaceDisplayRepo.deleteBySpaceId(id);
            spaceRepo.deleteById(id);
        }
    }

    @Override
    public List<SpaceLink> getOutgoingLinks(String id) {

        return spaceLinkRepo.getLinkedSpaces(id);
    }

    @Override
    public List<SpaceLink> getIncomingLinks(String id) {
        Optional<Space> space = spaceRepo.findById(id);
        if (space.isPresent()) {
            return spaceLinkRepo.findByTargetSpace(space.get());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ISpace> getSpacesWithImageId(String imageId) {
        if (imageId == null) {
            return null;
        }
        Optional<VSImage> vsImage = imageRepo.findById(imageId);
        if (!vsImage.isPresent()) {
            return null;
        }
        List<ISpace> spaces = new ArrayList<>();
        spaceRepo.findAllByImageId(imageId).forEach(space -> spaces.add(space));
        return spaces;
    }

    @Override
    public Iterable<Space> addIncomingLinkInfoToSpaces(Iterable<Space> spaces) {
        Iterator<Space> iterator = spaces.iterator();
        while (iterator.hasNext()) {
            Space space = iterator.next();
            space.setIncomingLinks((spaceLinkRepo.findByTargetSpace(space)).size() > 0 ? true : false);
        }
        return spaces;
    }
    
    @Override
    public Page<ISpace> findByNameOrDescription(Pageable requestedPage, String searchText) {
        return spaceRepo.findDistinctByNameContainingOrDescriptionContaining(requestedPage, searchText,searchText);
    }
}