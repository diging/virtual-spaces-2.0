package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
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
            ExternalLinkDisplayMode howToOpen)
            throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {

        /*
         * When createLink is called then inside updateLinkAndDisplay(link, displayLink)
         * is called to save Link and Display link in the database and return an object
         * of ExternalLinkDisplay. When setHowToOpen method is being called on the same
         * ExternalLinkDisplay object to set howToOpen field then Hibernate
         * automatically persist howToOpen in database.
         */
        IExternalLinkDisplay externalLinkDisplay = createLink(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;

    }

    @Override
    public IExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedId, String linkLabel, String linkId, String linkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename, ExternalLinkDisplayMode howToOpen)
            throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        /*
         * When updateLink is called then inside updateLinkAndDisplay(link, displayLink)
         * is called to save Link and Display link in the database and return an object
         * of ExternalLinkDisplay. When setHowToOpen method is being called on the same
         * ExternalLinkDisplay object to set howToOpen field then Hibernate
         * automatically persist howToOpen in database.
         */
        IExternalLinkDisplay externalLinkDisplay = updateLink(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, linkId, linkDisplayId, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;
    }

}
