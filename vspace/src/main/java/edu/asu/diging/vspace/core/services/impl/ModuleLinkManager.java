package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Transactional
@Service
public class ModuleLinkManager extends LinkManager<IModuleLink, IModule, IModuleLinkDisplay> implements IModuleLinkManager {

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
    public List<IModuleLinkDisplay> getLinkDisplays(String spaceId) {
        return moduleLinkDisplayRepo.findModuleLinkDisplaysForSpace(spaceId);
    }

    @Override
    protected IModule getTarget(String linkedModuleId) {
        return moduleManager.getModule(linkedModuleId);
    }

    @Override
    protected IModuleLink createLinkObject(String title, String id) {
        ISpace source = spaceManager.getSpace(id);
        return moduleLinkFactory.createModuleLink(title, source);
    }

    @Override
    protected IModuleLinkDisplay updateLinkAndDisplay(IModuleLink link, IModuleLinkDisplay displayLink) {
        moduleLinkRepo.save((ModuleLink) link);
        return moduleLinkDisplayRepo.save((ModuleLinkDisplay) displayLink);
    }

    @Override
    protected IModuleLinkDisplay getDisplayLink(String moduleLinkDisplayId) {
        Optional<ModuleLinkDisplay> moduleLinkDisplay = moduleLinkDisplayRepo.findById(moduleLinkDisplayId);
        if (moduleLinkDisplay.isPresent()) {
            return moduleLinkDisplay.get();
        }
        return null;
    }

    @Override
    protected IModuleLink getLink(String moduleLinkID) {
        Optional<ModuleLink> moduleLink = moduleLinkRepo.findById(moduleLinkID);
        if (moduleLink.isPresent()) {
            return moduleLink.get();
        }
        return null;
    }

    @Override
    protected IModuleLinkDisplay createDisplayLink(IModuleLink link) {
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

    @Override
    public HashSet<ISpace> findSpaceListFromModuleId(String moduleId) {
        List<ModuleLink> moduleLinks = moduleLinkRepo.findByModuleId(moduleId);
        return (HashSet<ISpace>) moduleLinks.stream().map(s->s.getSpace()).collect(Collectors.toSet());
    }

}
