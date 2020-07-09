package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class SpaceLinkManager extends LinkManager implements ISpaceLinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceLinkFactory spaceLinkFactory;

    @Autowired
    private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @Autowired
    private SpaceLinkRepository spaceLinkRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    @Override
    public ISpaceLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }

        ISpace target = spaceManager.getSpace(linkedSpaceId);
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
        link.setTargetSpace(target);
        link.setName(spaceLinkLabel);
        spaceLinkRepo.save((SpaceLink) link);

        ISpaceLinkDisplay display = spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setRotation(rotation);
        display.setType(displayType != null ? displayType : DisplayType.ARROW);

        if (linkImage != null && linkImage.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(linkImage);

            IVSImage image = imageFactory.createImage(imageFilename, contentType);
            image = imageRepo.save((VSImage) image);

            String relativePath = null;
            try {
                relativePath = storage.storeFile(linkImage, imageFilename, image.getId());
            } catch (FileStorageException e) {
                throw new ImageCouldNotBeStoredException(e);
            }
            image.setParentPath(relativePath);
            imageRepo.save((VSImage) image);

            display.setImage(image);
        }

        spaceLinkDisplayRepo.save((SpaceLinkDisplay) display);
        return display;
    }

    @Override
    public List<SpaceLinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(spaceId));
    }

    @Override
    public ISpaceLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedSpaceId, String spaceLinkLabel, String spaceLinkIdValueEdit, String spaceLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        spaceValidation(id);

        Optional<SpaceLink> linkOptional = spaceLinkRepo.findById(spaceLinkIdValueEdit);
        Optional<SpaceLinkDisplay> spaceLinkOptional = spaceLinkDisplayRepo.findById(spaceLinkDisplayId);

        linksValidation(linkOptional, spaceLinkOptional);

        ISpaceLink link = linkOptional.get();
        ISpaceLinkDisplay display = spaceLinkOptional.get();

        link.setName(title);
        ISpace space = spaceManager.getSpace(linkedSpaceId);
        link.setTargetSpace(space);

        populateDisplay((ILinkDisplay)display,positionX,positionY,rotation, displayType, linkImage, imageFilename);

        spaceLinkRepo.save((SpaceLink) link);
        spaceLinkDisplayRepo.save((SpaceLinkDisplay) display);

        return display;
    }

    @Override
    public void deleteLink(String linkId) {
        Optional<SpaceLink> linkOptional = spaceLinkRepo.findById(linkId);
        if (!linkOptional.isPresent()) {
            return;
        }

        ISpace space = linkOptional.get().getSourceSpace();
        ISpaceLink link = linkOptional.get();
        space.getSpaceLinks().remove(link);
        spaceLinkDisplayRepo.deleteByLink(link);
        spaceLinkRepo.delete((SpaceLink) link);

    }

}
