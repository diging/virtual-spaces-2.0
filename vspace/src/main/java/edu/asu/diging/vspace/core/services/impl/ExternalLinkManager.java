package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExternalLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.factory.IExternalLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IExternalLinkFactory;
import edu.asu.diging.vspace.core.model.IExternalLink;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
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
    public List<ExternalLinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(externalLinkDisplayRepo.findExternalLinkDisplaysForSpace(spaceId));
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
        ExternalLinkValue value = new ExternalLinkValue(externalLink);
        return value;
    }

    @Override
    protected ILinkDisplay saveLinkAndDisplay(ILink link, ILinkDisplay displayLink) {
        externalLinkRepo.save((ExternalLink) link);
        externalLinkDisplayRepo.save((ExternalLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(ILink link, IVSpaceElement target) {
        ((IExternalLink) link).setExternalLink(((ExternalLinkValue)target).getValue());

    }

    @Override
    protected ILinkDisplay getDisplayLink(String externalLinkDisplayId) throws LinkDoesNotExistsException {
        Optional<ExternalLinkDisplay> externalLinkOptional = externalLinkDisplayRepo.findById(externalLinkDisplayId);
        linksValidation(externalLinkOptional);
        IExternalLinkDisplay display = externalLinkOptional.get();
        return display;
    }

    @Override
    protected ILink getLink(String externalLinkIDValueEdit) throws LinkDoesNotExistsException {
        Optional<ExternalLink> linkOptional = externalLinkRepo.findById(externalLinkIDValueEdit);
        linksValidation(linkOptional);
        IExternalLink link = linkOptional.get();
        return link;
    }

    @Override
    protected ILinkDisplay createLinkDisplay(ILink link) {
        ILinkDisplay display = externalLinkDisplayFactory.createExternalLinkDisplay((IExternalLink)link);
        return display;
    }

}
