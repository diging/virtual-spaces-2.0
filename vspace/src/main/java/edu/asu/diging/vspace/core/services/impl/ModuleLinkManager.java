package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.DisplayType;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ModuleLinkManager extends LinkManager implements IModuleLinkManager{

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private IModuleLinkFactory moduleLinkFactory;

    @Autowired
    private IModuleLinkDisplayFactory moduleLinkDisplayFactory;

    @Autowired
    private ModuleLinkRepository moduleLinkRepo;

    @Autowired
    private ModuleLinkDisplayRepository moduleLinkDisplayRepo;

    @Override
    public List<ILinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(moduleLinkDisplayRepo.findModuleLinkDisplaysForSpace(spaceId));
    }

    @Override
    protected IModule getTarget(String linkedModuleId) {
        return moduleManager.getModule(linkedModuleId);
    }

    @Override
    protected ILink createLinkObject(String title, String id, IVSpaceElement target, String linkLabel) {
        ISpace source = spaceManager.getSpace(id);
        IModuleLink link = moduleLinkFactory.createModuleLink(title, source);
        link.setModule((IModule) target);
        link.setName(linkLabel);
        return link;
    }

    @Override
    protected ILinkDisplay updateLinkAndDisplay(ILink link, ILinkDisplay displayLink) {
        moduleLinkRepo.save((ModuleLink) link);
        moduleLinkDisplayRepo.save((ModuleLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(ILink link, IVSpaceElement target) {
        link.setTarget(target);

    }

    @Override
    protected ILinkDisplay getDisplayLink(String moduleLinkDisplayId){
        Optional<ModuleLinkDisplay> moduleLinkOptional = moduleLinkDisplayRepo.findById(moduleLinkDisplayId);
        if(!linksValidation(moduleLinkOptional)) {
            return null;
        }
        return moduleLinkOptional.get();
    }

    @Override
    protected ILink getLink(String moduleLinkID){
        Optional<ModuleLink> linkOptional = moduleLinkRepo.findById(moduleLinkID);
        if(!linksValidation(linkOptional)) {
            return null;
        }
        return linkOptional.get();
    }

    @Override
    protected ILinkDisplay saveDisplayLinkRepo(ILink link, float positionX, float positionY, int rotation,
            DisplayType displayType, byte[] linkImage, String imageFilename) throws ImageCouldNotBeStoredException {
        ILinkDisplay displayLink = moduleLinkDisplayFactory.createModuleLinkDisplay((IModuleLink)link);
        setDisplayProperties(displayLink, positionX, positionY, rotation, displayType, linkImage, imageFilename);
        moduleLinkRepo.save((ModuleLink) link);
        moduleLinkDisplayRepo.save((ModuleLinkDisplay) displayLink);
        return displayLink;

    }

    @Override
    protected void deleteLinkDisplayRepo(ILink link) {
        moduleLinkDisplayRepo.deleteByLink((IModuleLink)link);
    }

    @Override
    protected void removeFromLinkList(ISpace space, ILink link) {
        space.getModuleLinks().remove((IModuleLink)link);
    }

    @Override
    protected void deleteLinkRepo(ILink link) {
        moduleLinkRepo.delete((ModuleLink)link);
    }



}
