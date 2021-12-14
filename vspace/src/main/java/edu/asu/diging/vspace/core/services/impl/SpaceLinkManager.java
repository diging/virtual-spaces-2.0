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
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Transactional
@Service
public class SpaceLinkManager extends LinkManager<ISpaceLink,ISpace,ISpaceLinkDisplay> implements ISpaceLinkManager{

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
    public List<ISpaceLinkDisplay> getLinkDisplays(String spaceId) {
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
    protected ISpaceLinkDisplay getDisplayLink(String spaceLinkDisplayId){
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
    protected ISpaceLinkDisplay updateLinkAndDisplay(ISpaceLink link, ISpaceLinkDisplay displayLink) {
        spaceLinkRepo.save((SpaceLink) link);
        return spaceLinkDisplayRepo.save((SpaceLinkDisplay) displayLink);
    }

    @Override
    protected ISpaceLinkDisplay createDisplayLink(ISpaceLink link){
        return spaceLinkDisplayFactory.createSpaceLinkDisplay(link);
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
    
    @Override
    public List<ISpaceLinkDisplay> getSpaceLinkForGivenOrNullSpaceStatus(String spaceId, SpaceStatus spaceStatus){
        return new ArrayList<>(spaceLinkDisplayRepo.findSpaceLinksForGivenOrNullSpaceStatus(spaceId,spaceStatus));
    }
    
    @Override
    public void deleteSpaceLinksWithSourceAsNull(){
        List<ISpaceLink> spaceLinks = spaceLinkRepo.findBySourceSpaceIsNull();
        //need to delete all space link displays before deleting spacelink
        for(ISpaceLink spaceLink : spaceLinks) {
            spaceLinkDisplayRepo.deleteByLink(spaceLink);
        }
        spaceLinkRepo.deleteBySourceSpaceIdIsNull();
    }
    
    @Override
    public List<ISpaceLink> findSpaceLinksWithSourceNull(){
        return spaceLinkRepo.findBySourceSpaceIsNull();
    }
    

}
