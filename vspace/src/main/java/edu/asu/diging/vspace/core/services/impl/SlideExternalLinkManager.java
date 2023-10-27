package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.data.SlideExternalLinkRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SlideExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISlideExternalLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.IExternalLinkSlide;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkSlide;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class SlideExternalLinkManager extends SlideLinkManager<IExternalLinkSlide, ExternalLinkValue, ISlideExternalLinkDisplay>
implements ISlideExternalLinkManager{
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private SlideExternalLinkRepository externalLinkRepo;

    @Autowired
    private SlideExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Autowired
    private ISlideExternalLinkFactory externalLinkFactory;

    @Autowired
    private ISlideExternalLinkDisplayFactory externalLinkDisplayFactory;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IStorageEngine storage;
    
    @Override
    public List<ISlideExternalLinkDisplay> getLinkDisplays(String slideId) {
        return externalLinkDisplayRepo.findSlideExternalLinkDisplaysForSlide(slideId);
    }

    @Override
    protected IExternalLinkSlide createLinkObject(String title, String id) {
        ISlide source = slideManager.getSlide(id);
        IExternalLinkSlide link = externalLinkFactory.createExternalLink(title, source);
        return externalLinkRepo.save((ExternalLinkSlide) link);
    }
    
    @Override
    protected ExternalLinkValue getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
    }

    @Override
    protected ISlideExternalLinkDisplay updateLinkAndDisplay(IExternalLinkSlide link, ISlideExternalLinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLinkSlide) link);
        return externalLinkDisplayRepo.save((SlideExternalLinkDisplay) displayLink);
    }

    @Override
    protected ISlideExternalLinkDisplay getDisplayLink(String externalLinkDisplayId) {
        Optional<SlideExternalLinkDisplay> externalLinkDisplay = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        if (externalLinkDisplay.isPresent()) {
            return externalLinkDisplay.get();
        }
        return null;
    }

    @Override
    protected IExternalLinkSlide getLink(String externalLinkID) {
        Optional<ExternalLinkSlide> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
    }

    @Override
    protected ISlideExternalLinkDisplay createDisplayLink(IExternalLinkSlide link) {
        return externalLinkDisplayFactory.createExternalLinkDisplay(link);
    }

    @Override
    protected void deleteLinkDisplayRepo(IExternalLinkSlide link) {
        externalLinkDisplayRepo.deleteByExternalLink(link);
    }

    @Override
    protected void removeFromLinkList(ISlide slide, IExternalLinkSlide link) {
        slide.getExternalLinks().remove(link);
    }

    @Override
    protected void deleteLinkRepo(IExternalLinkSlide link) {
        externalLinkRepo.delete((ExternalLinkSlide) link);
    }

    
    @Override
    public ISlideExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedId, String linkLabel, DisplayType displayType, byte[] linkImage, String imageFilename,
            ExternalLinkDisplayMode howToOpen)
            throws SlideDoesNotExistException, ImageCouldNotBeStoredException, SlideDoesNotExistException {

        /*
         * When createLink is called then inside updateLinkAndDisplay(link, displayLink)
         * is called to save Link and Display link in the database and return an object
         * of ExternalLinkDisplay. When setHowToOpen method is being called on the same
         * ExternalLinkDisplay object to set howToOpen field then Hibernate
         * automatically persist howToOpen in database.
         */
        ISlideExternalLinkDisplay externalLinkDisplay = createLink(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;

    }

    @Override
    public ISlideExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedId, String linkLabel, String linkId, String linkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename, ExternalLinkDisplayMode howToOpen)
            throws SlideDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        /*
         * When updateLink is called then inside updateLinkAndDisplay(link, displayLink)
         * is called to save Link and Display link in the database and return an object
         * of ExternalLinkDisplay. When setHowToOpen method is being called on the same
         * ExternalLinkDisplay object to set howToOpen field then Hibernate
         * automatically persist howToOpen in database.
         */
        ISlideExternalLinkDisplay externalLinkDisplay = updateLink(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, linkId, linkDisplayId, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;
    }

}
