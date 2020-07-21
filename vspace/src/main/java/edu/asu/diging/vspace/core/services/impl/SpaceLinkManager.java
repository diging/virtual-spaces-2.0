package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Transactional
@Service
public class SpaceLinkManager extends LinkManager<ISpaceLink,ISpace> implements ISpaceLinkManager<ISpaceLink,ISpace>{

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
    protected ISpaceLink createLinkObject(String title, String id, ISpace target, String spaceLinkLabel) {

        ISpace source = spaceManager.getSpace(id);
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
        link.setTarget(target);
        link.setName(spaceLinkLabel);
        spaceLinkRepo.save((SpaceLink) link);
        return link;
    }

    @Override
    protected ISpace getTarget(String linkedSpaceId) {
        return spaceManager.getSpace(linkedSpaceId);
    }

    @Override
    protected ILinkDisplay getDisplayLink(String spaceLinkDisplayId){
        Optional<SpaceLinkDisplay> spaceLinkOptional = spaceLinkDisplayRepo.findById(spaceLinkDisplayId);
        if(!linksValidation(spaceLinkOptional)) {
            return null;
        }
        return spaceLinkOptional.get();
    }

    @Override
    protected ISpaceLink getLink(String spaceLinkId){
        Optional<SpaceLink> linkOptional = spaceLinkRepo.findById(spaceLinkId);
        if(!linksValidation(linkOptional)) {
            return null;
        }
        return linkOptional.get();
    }

    @Override
    protected void setTarget(ISpaceLink link, ISpace target) {
        link.setTarget(target);
    }

    @Override
    protected ILinkDisplay updateLinkAndDisplay(ISpaceLink link, ILinkDisplay displayLink) {
        spaceLinkRepo.save((SpaceLink) link);
        spaceLinkDisplayRepo.save((SpaceLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected ILinkDisplay createDisplayLink(ISpaceLink link){
        return spaceLinkDisplayFactory.createSpaceLinkDisplay((ISpaceLink)link);
    }

    @Override
    protected void deleteLinkDisplayRepo(ISpaceLink link) {
        spaceLinkDisplayRepo.deleteByLink(link);
    }

    @Override
    protected void removeFromLinkList(ISpace space, ISpaceLink link) {
        space.getSpaceLinks().remove(link);
    }

    @Override
    protected void deleteLinkRepo(ISpaceLink link) {
        spaceLinkRepo.delete((SpaceLink)link);
    }

}
