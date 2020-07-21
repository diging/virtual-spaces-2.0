package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ModuleLinkManager extends LinkManager<IModuleLink,IModule> implements IModuleLinkManager<IModuleLink,IModule>{

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
    protected IModuleLink createLinkObject(String title, String id, IModule target, String linkLabel) {
        ISpace source = spaceManager.getSpace(id);
        IModuleLink link = moduleLinkFactory.createModuleLink(title, source);
        link.setModule(target);
        link.setName(linkLabel);
        return link;
    }

    @Override
    protected ILinkDisplay updateLinkAndDisplay(IModuleLink link, ILinkDisplay displayLink) {
        moduleLinkRepo.save((ModuleLink) link);
        moduleLinkDisplayRepo.save((ModuleLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(IModuleLink link, IModule target) {
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
    protected IModuleLink getLink(String moduleLinkID){
        Optional<ModuleLink> linkOptional = moduleLinkRepo.findById(moduleLinkID);
        if(!linksValidation(linkOptional)) {
            return null;
        }
        return linkOptional.get();
    }

    @Override 
    protected ILinkDisplay createDisplayLink(IModuleLink link) {
        return moduleLinkDisplayFactory.createModuleLinkDisplay(link);
    }

    @Override
    protected void deleteLinkDisplayRepo(IModuleLink link) {
        moduleLinkDisplayRepo.deleteByLink(link);
    }

    @Override
    protected void removeFromLinkList(ISpace space, IModuleLink link) {
        space.getModuleLinks().remove(link);
    }

    @Override
    protected void deleteLinkRepo(IModuleLink link) {
        moduleLinkRepo.delete((ModuleLink) link);
    }

}
