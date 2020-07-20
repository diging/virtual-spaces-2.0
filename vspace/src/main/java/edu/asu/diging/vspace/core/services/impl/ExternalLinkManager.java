package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
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

    @Override
    public List<ILinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(spaceId));
    }

    @Override
    protected ILink createLinkObject(String title, String id, IVSpaceElement target, String linkLabel) {
        ISpace source = spaceManager.getSpace(id);
        IExternalLink link = externalLinkFactory.createExternalLink(title, source, ((ExternalLinkValue)target).getValue());
        link.setName(linkLabel);
        externalLinkRepo.save((ExternalLink) link);
        return link;
    }

    @Override
    protected IVSpaceElement getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
    }

    @Override
    protected ILinkDisplay updateLinkAndDisplay(ILink link, ILinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLink) link);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(ILink link, IVSpaceElement target) {
        link.setTarget(target);

    }

    @Override
    protected ILinkDisplay getDisplayLink(String externalLinkDisplayId){
        Optional<ExternalLinkDisplay> externalLinkOptional = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        if(!linksValidation(externalLinkOptional)) {
            return null;
        }
        IExternalLinkDisplay display = externalLinkOptional.get();
        return display;
    }

    @Override
    protected ILink getLink(String externalLinkID){
        Optional<ExternalLink> linkOptional = externalLinkRepo.findById(externalLinkID);
        if(!linksValidation(linkOptional)) {
            return null;
        }
        return (ILink) linkOptional.get();
    }

    @Override
    protected ILinkDisplay saveDisplayLinkRepo(ILink link, float positionX, float positionY, int rotation,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException {
        ILinkDisplay displayLink = externalLinkDisplayFactory.createExternalLinkDisplay((IExternalLink)link);
        setDisplayProperties(displayLink, positionX, positionY, rotation, displayType, linkImage, imageFilename);
        externalLinkRepo.save((ExternalLink) link);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void deleteLinkDisplayRepo(ILink link) {
        externalLinkDisplayRepo.deleteByExternalLink((IExternalLink)link);
    }

    @Override
    protected void removeFromLinkList(ISpace space, ILink link) {
        space.getExternalLinks().remove((IExternalLink)link);
    }

    @Override
    protected void deleteLinkRepo(ILink link) {
        externalLinkRepo.delete((ExternalLink)link);
    }

}
