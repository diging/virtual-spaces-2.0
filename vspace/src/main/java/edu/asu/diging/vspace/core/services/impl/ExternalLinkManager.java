package edu.asu.diging.vspace.core.services.impl;

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
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ExternalLinkManager extends LinkManager<IExternalLink, ExternalLinkValue, IExternalLinkDisplay>
        implements IExternalLinkManager {

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
    public List<IExternalLinkDisplay> getLinkDisplays(String spaceId) {
        return externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(spaceId);
    }

    @Override
    protected IExternalLink createLinkObject(String title, String id) {
        ISpace source = spaceManager.getSpace(id);
        IExternalLink link = externalLinkFactory.createExternalLink(title, source);
        return externalLinkRepo.save((ExternalLink) link);
    }

    @Override
    protected ExternalLinkValue getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
    }

    @Override
    protected IExternalLinkDisplay updateLinkAndDisplay(IExternalLink link, IExternalLinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLink) link);
        return externalLinkDisplayRepo.save((ExternalLinkDisplay) displayLink);
    }

    @Override
    protected IExternalLinkDisplay getDisplayLink(String externalLinkDisplayId) {
        Optional<ExternalLinkDisplay> externalLinkDisplay = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        if (externalLinkDisplay.isPresent()) {
            return externalLinkDisplay.get();
        }
        return null;
    }

    @Override
    protected IExternalLink getLink(String externalLinkID) {
        Optional<ExternalLink> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
    }

    @Override
    protected IExternalLinkDisplay createDisplayLink(IExternalLink link) {
        return externalLinkDisplayFactory.createExternalLinkDisplay(link);
    }

    @Override
    protected void deleteLinkDisplayRepo(IExternalLink link) {
        externalLinkDisplayRepo.deleteByExternalLink(link);
    }

    @Override
    protected void removeFromLinkList(ISpace space, IExternalLink link) {
        space.getExternalLinks().remove(link);
    }

    @Override
    protected void deleteLinkRepo(IExternalLink link) {
        externalLinkRepo.delete((ExternalLink) link);
    }

    @Override
    public IExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedId, String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename,
            String howToOpen)
            throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {

        IExternalLink link = createLinkObject(title, id);
        ExternalLinkValue target = getTarget(linkedId);
        link.setName(linkLabel);
        link.setTarget(target);
        IExternalLinkDisplay displayLink = createDisplayLink(link);
        setDisplayProperties(displayLink, id, positionX, positionY, rotation, displayType, linkImage, imageFilename,
                howToOpen);
        return updateLinkAndDisplay(link, displayLink);

    }

    @Override
    public IExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedId, String linkLabel, String linkId, String linkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename, String howToOpen)
            throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        validateSpace(id);

        IExternalLink link = getLink(linkId);
        ExternalLinkValue target = getTarget(linkedId);
        link.setName(title);
        link.setTarget(target);
        IExternalLinkDisplay displayLink = getDisplayLink(linkDisplayId);
        setDisplayProperties(displayLink, id, positionX, positionY, rotation, displayType, linkImage, imageFilename,
                howToOpen);
        return updateLinkAndDisplay(link, displayLink);
    }

    protected void setDisplayProperties(ILinkDisplay linkDisplay, String id, float positionX, float positionY,
            int rotation, DisplayType displayType, byte[] linkImage, String imageFilename, String howToOpen)
            throws ImageCouldNotBeStoredException {
        linkDisplay.setPositionX(positionX);
        linkDisplay.setPositionY(positionY);
        linkDisplay.setRotation(rotation);
        linkDisplay.setHowToOpen(howToOpen);
        linkDisplay.setType(displayType != null ? displayType : DisplayType.ARROW);
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
            linkDisplay.setImage(image);
        }

    }
}
