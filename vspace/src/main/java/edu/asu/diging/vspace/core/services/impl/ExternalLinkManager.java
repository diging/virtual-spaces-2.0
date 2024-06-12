package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ExternalLinkDisplayMode;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
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
    private IExternalLinkFactory externalLinkFactory;   

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
    protected IExternalLink getLink(String externalLinkID) {
        Optional<ExternalLink> externalLink = externalLinkRepo.findById(externalLinkID);
        if (externalLink.isPresent()) {
            return externalLink.get();
        }
        return null;
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

    @Override
    public List<IExternalLinkDisplay> getLinkDisplays(String spaceId) {
        return null;
    }

    @Override
    protected void deleteLinkDisplayRepo(IExternalLink link) {        
    }

    @Override
    protected IExternalLinkDisplay updateLinkAndDisplay(IExternalLink link, IExternalLinkDisplay displayLink) {
        return null;
    }

    @Override
    protected IExternalLinkDisplay getDisplayLink(String linkDisplayId) throws LinkDoesNotExistsException {
        return null;
    }

    @Override
    protected IExternalLinkDisplay createDisplayLink(IExternalLink link) {
        return null;
    }

}
