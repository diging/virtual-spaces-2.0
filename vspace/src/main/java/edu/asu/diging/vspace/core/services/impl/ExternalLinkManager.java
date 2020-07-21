package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ExternalLinkManager extends LinkManager<IExternalLink, ExternalLinkValue> implements IExternalLinkManager<IExternalLink, ExternalLinkValue>{

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
    protected IExternalLink createLinkObject(String title, String id, ExternalLinkValue target, String linkLabel) {
        ISpace source = spaceManager.getSpace(id);
        IExternalLink link = externalLinkFactory.createExternalLink(title, source, target.getValue());
        link.setName(linkLabel);
        externalLinkRepo.save((ExternalLink) link);
        return link;
    }

    @Override
    protected ExternalLinkValue getTarget(String externalLink) {
        return new ExternalLinkValue(externalLink);
    }

    @Override
    protected ILinkDisplay updateLinkAndDisplay(IExternalLink link, ILinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLink) link);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(IExternalLink link, ExternalLinkValue target) {
        link.setTarget(target);

    }

    @Override
    protected ILinkDisplay getDisplayLink(String externalLinkDisplayId){
        Optional<ExternalLinkDisplay> externalLinkOptional = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        if(!linksValidation(externalLinkOptional)) {
            return null;
        }
        return externalLinkOptional.get();
    }

    @Override
    protected IExternalLink getLink(String externalLinkID){
        Optional<ExternalLink> linkOptional = externalLinkRepo.findById(externalLinkID);
        if(!linksValidation(linkOptional)) {
            return null;
        }
        return linkOptional.get();
    }

    @Override
    protected ILinkDisplay createDisplayLink(IExternalLink link) {
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
        externalLinkRepo.delete((ExternalLink)link);
    }

}
