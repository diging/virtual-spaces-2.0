package edu.asu.diging.vspace.core.services.impl;

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
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Transactional
@Service
public class SpaceLinkManager extends LinkManager<ISpaceLink,ISpace,SpaceLinkDisplay> implements ISpaceLinkManager<ISpaceLink,ISpace,SpaceLinkDisplay>{

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
    public List<SpaceLinkDisplay> getLinkDisplays(String spaceId) {
        return spaceLinkDisplayRepo.findSpaceLinkDisplaysForSpace(spaceId);
    }

    @Override
    protected ISpaceLink createLinkObject(String title, String id) {

        ISpace source = spaceManager.getSpace(id);
        ISpaceLink link = spaceLinkFactory.createSpaceLink(title, source);
        return spaceLinkRepo.save((SpaceLink) link);
    }

    @Override
    protected ISpace getTarget(String linkedSpaceId) {
        return spaceManager.getSpace(linkedSpaceId);
    }

    @Override
    protected SpaceLinkDisplay getDisplayLink(String spaceLinkDisplayId){
        Optional<SpaceLinkDisplay> spaceLinkDisplay = spaceLinkDisplayRepo.findById(spaceLinkDisplayId);
        if(spaceLinkDisplay.isPresent()) {
            return spaceLinkDisplay.get();
        }

        return null;
    }

    @Override
    protected ISpaceLink getLink(String spaceLinkId){
        Optional<SpaceLink> spaceLink = spaceLinkRepo.findById(spaceLinkId);
        if(spaceLink.isPresent()) {
            return spaceLink.get();
        }
        return null;
    }

    @Override
    protected SpaceLinkDisplay updateLinkAndDisplay(ISpaceLink link, SpaceLinkDisplay displayLink) {
        spaceLinkRepo.save((SpaceLink) link);
        return spaceLinkDisplayRepo.save(displayLink);
    }

    @Override
    protected SpaceLinkDisplay createDisplayLink(ISpaceLink link){
        return (SpaceLinkDisplay) spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
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
