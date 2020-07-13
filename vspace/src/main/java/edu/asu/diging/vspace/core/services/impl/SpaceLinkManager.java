package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
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

    @Override
    public List<ILinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(spaceId));
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

    @Override
    protected ILink createLinkObject(String title, String id, IVSpaceElement target, String spaceLinkLabel) {

        ISpace source = spaceManager.getSpace(id);
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
        link.setTarget((ISpace)target);
        link.setName(spaceLinkLabel);
        spaceLinkRepo.save((SpaceLink) link);
        return (ILink) link;
    }

    @Override
    protected IVSpaceElement getTarget(String linkedSpaceId) {
        ISpace target = spaceManager.getSpace(linkedSpaceId);
        return target;
    }

    @Override
    protected ILinkDisplay getDisplayLink(String spaceLinkDisplayId) throws LinkDoesNotExistsException{
        Optional<SpaceLinkDisplay> spaceLinkOptional = spaceLinkDisplayRepo.findById(spaceLinkDisplayId);
        linksValidation(spaceLinkOptional);
        ISpaceLinkDisplay display = spaceLinkOptional.get();
        return display;
    }

    @Override
    protected ILink getLink(String spaceLinkIdValueEdit) throws LinkDoesNotExistsException {
        Optional<SpaceLink> linkOptional = spaceLinkRepo.findById(spaceLinkIdValueEdit);
        linksValidation(linkOptional);
        ISpaceLink link = linkOptional.get();
        return link;
    }

    @Override
    protected void setTarget(ILink link, IVSpaceElement target) {
        ((ISpaceLink)link).setTargetSpace((ISpace)target);
    }

    @Override
    protected ILinkDisplay saveLinkAndDisplay(ILink link, ILinkDisplay displayLink) {
        spaceLinkRepo.save((SpaceLink) link);
        spaceLinkDisplayRepo.save((SpaceLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected ILinkDisplay createLinkDisplay(ILink link) {
        ILinkDisplay display = spaceLinkDisplayFactory.createSpaceLinkDisplay((ISpaceLink)link);
        return display;
    }

}
