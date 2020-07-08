package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ExternalLinkManager extends LinkManager implements IExternalLinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ExternalLinkRepository externalLinkRepo;

    @Autowired
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Autowired
    private IExternalLinkFactory externalLinkFactory;

    @Autowired
    private IExternalLinkDisplayFactory externalLinkDisplayFactory;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;

    @Override
    public IExternalLinkDisplay createLink(String title, ISpace source, float positionX, float positionY, int rotation,
            String externalLink, String externalLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {
        source = spaceManager.getSpace(source.getId());
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }
        IExternalLink link = externalLinkFactory.createExternalLink(title, source, externalLink);
        externalLinkRepo.save((ExternalLink) link);

        IExternalLinkDisplay display = externalLinkDisplayFactory.createExternalLinkDisplay(link);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setName(title);
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

        externalLinkDisplayRepo.save((ExternalLinkDisplay) display);
        return display;
    }

    @Override
    public List<ExternalLinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(spaceId));
    }

    @Override
    public IExternalLinkDisplay editLink(String title, String id, float positionX, float positionY, int rotation,
            String externalLink, String externalLinkLabel, String externalLinkIdValueEdit, String externalLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {
        spaceValidation(id);

        Optional<ExternalLink> linkOptional = externalLinkRepo.findById(externalLinkIdValueEdit);
        Optional<ExternalLinkDisplay> externalLinkOptional = externalLinkDisplayRepo.findById(externalLinkDisplayId);

        linksValidation(linkOptional, externalLinkOptional);

        IExternalLink link = linkOptional.get();
        IExternalLinkDisplay display = externalLinkOptional.get();

        link.setName(title);
        link.setExternalLink(externalLink);

        populateDisplay((ILinkDisplay)display,positionX,positionY,0, displayType, linkImage, imageFilename);

        externalLinkRepo.save((ExternalLink) link);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) display);

        return display;
    }

    @Override
    public void deleteLink(String linkId) {
        Optional<ExternalLink> linkOptional = externalLinkRepo.findById(linkId);
        if (!linkOptional.isPresent()) {
            return;
        }

        ISpace space = linkOptional.get().getSpace();
        IExternalLink link = linkOptional.get();
        space.getExternalLinks().remove(link);
        externalLinkDisplayRepo.deleteByExternalLink(link);
        externalLinkRepo.delete((ExternalLink) link);

    }

}
