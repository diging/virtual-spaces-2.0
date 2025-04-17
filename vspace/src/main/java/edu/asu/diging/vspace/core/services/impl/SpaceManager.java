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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import edu.asu.diging.vspace.core.factory.ILocalizedTextFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.SortByField;
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
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

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
    
    @Autowired
    private ILocalizedTextFactory localizedTextFactory;
    
    @Value("${page_size}")
    private int pageSize;

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
    
    @Override
    public List<ISpace> findByName(String searchText){
        String searchTerm = "%" + searchText + "%";
        List<Space> spaces = spaceRepo.findByNameLike(searchTerm);
        List<ISpace> spaceResults = new ArrayList<>();
        spaces.forEach(r -> spaceResults.add(r));
        return spaceResults;
    }
    
    @Override
    public List<ISpace> getSpaces(int pageNo) {
        return getSpaces(pageNo, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
    }
    /**
     * Method to return the requested spaces
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of images in the requested pageNo and requested order.
     */
    @Override
    public List<ISpace> getSpaces(int pageNo, String sortedBy, String order) {
        Sort sortingParameters = getSortingParameters(sortedBy, order);
        if(pageNo < 1) {
            pageNo = 1;
        }
        Pageable pagable = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        Page<Space> spaces = spaceRepo.findAll(pagable);
        if(spaces.getContent().size() == 0) {
            pagable = PageRequest.of(spaces.getTotalPages() - 1, pageSize, sortingParameters);
            spaces = spaceRepo.findAll(pagable);
        }
        List<ISpace> results = new ArrayList<>();
        if(spaces != null) {
            spaces.getContent().forEach(i -> results.add(i));
        }
        return results;
    }
    
    private Sort getSortingParameters(String sortedBy, String order) {
        Sort sortingParameters = Sort.by(SortByField.CREATION_DATE.getValue()).descending();
        if(sortedBy!=null && SortByField.getAllValues().contains(sortedBy)) {
            sortingParameters = Sort.by(sortedBy);
        }
        if(order!=null && order.equalsIgnoreCase(Sort.Direction.ASC.toString())) {
            sortingParameters = sortingParameters.ascending();
        } else {
            sortingParameters = sortingParameters.descending();
        }
        return sortingParameters;
    }

    @Override
    public void updateNameAndDescription(ISpace space, SpaceForm spaceForm) {
        space.setName(spaceForm.getDefaultName().getText());
        space.setDescription(spaceForm.getDefaultDescription().getText());
        List<ILocalizedText> names = space.getSpaceNames();
        List<ILocalizedText> descriptions = space.getSpaceDescriptions();
        
        addSpaceDetails(space,spaceForm.getDefaultName(), names);
        addSpaceDetails(space,spaceForm.getDefaultDescription(), descriptions);
        for(LocalizedTextForm title:spaceForm.getNames()) {   
            addSpaceDetails(space,title, names);
        }
        for(LocalizedTextForm text:spaceForm.getDescriptions()) {
            addSpaceDetails(space,text, descriptions);
        }       
    }
    
    /**
     * Adds name to spaceNames List of the given space object.
     * @param space The space to which the name will be added.
     * @param name The localized text form containing the name to be added.
     */
    @Override
    public void addSpaceDetails(ISpace space, LocalizedTextForm name, List<ILocalizedText> localizedTextList) {
        localizedTextFactory.createOrUpdateLocalizedText(space, name, localizedTextList);
    }
    
}