package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IExternalLinkSlideManager;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Transactional
@Service
public class ExternalLinkSlideManager extends LinkManager<IExternalLink, ExternalLinkValue, IExternalLinkDisplay> 
        implements IExternalLinkSlideManager {
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private IExternalLinkFactory externalLinkFactory;
    
    @Autowired
    private ExternalLinkRepository externalLinkRepo;
    
    @Autowired
    private ExternalLinkDisplayRepository externalLinkDisplayRepo;

    @Override
    public List<IExternalLinkDisplay> getLinkDisplays(String slideId) {
        return externalLinkDisplayRepo.findExternalLinkDisplaysForSlide(slideId);
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
    protected IExternalLink createLinkObject(String title, String id) {
        ISlide source = slideManager.getSlide(id);
        IExternalLink link = externalLinkFactory.createExternalLink(title, source);
        return externalLinkRepo.save((ExternalLink) link);
    }
    
    @Override
    public IExternalLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation,
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
        IExternalLinkDisplay externalLinkDisplay = createLinkForSlide(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;

    }

    @Override
    public IExternalLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
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
        IExternalLinkDisplay externalLinkDisplay = updateLink(title, id, positionX, positionY, rotation, linkedId,
                linkLabel, linkId, linkDisplayId, displayType, linkImage, imageFilename);
        externalLinkDisplay.setHowToOpen(howToOpen);
        return externalLinkDisplay;
    }
    

}
