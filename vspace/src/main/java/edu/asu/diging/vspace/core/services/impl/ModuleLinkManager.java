package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ModuleLinkRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.ILink;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
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
    public List<ModuleLinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(moduleLinkDisplayRepo.findModuleLinkDisplaysForSpace(spaceId));
    }

    @Override
    public void deleteLink(String linkId) {
        Optional<ModuleLink> linkOptional = moduleLinkRepo.findById(linkId);
        if (!linkOptional.isPresent()) {
            return;
        }

        ISpace space = linkOptional.get().getSpace();
        IModuleLink link = linkOptional.get();
        space.getModuleLinks().remove(link);
        moduleLinkDisplayRepo.deleteByLink(link);
        moduleLinkRepo.delete((ModuleLink) link);

    }

    @Override
    protected IModule getTarget(String linkedModuleId) {
        IModule target = moduleManager.getModule(linkedModuleId);
        return target;
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
    protected ILinkDisplay saveLinkAndDisplay(ILink link, ILinkDisplay displayLink) {
        moduleLinkRepo.save((ModuleLink) link);
        moduleLinkDisplayRepo.save((ModuleLinkDisplay) displayLink);
        return displayLink;
    }

    @Override
    protected void setTarget(ILink link, IVSpaceElement target) {
        ((IModuleLink) link).setTarget((IModule)target);

    }

    @Override
    protected ILinkDisplay getDisplayLink(String moduleLinkDisplayId) throws LinkDoesNotExistsException {
        Optional<ModuleLinkDisplay> moduleLinkOptional = moduleLinkDisplayRepo.findById(moduleLinkDisplayId);
        linksValidation(moduleLinkOptional);
        IModuleLinkDisplay display = moduleLinkOptional.get();
        return display;
    }

    @Override
    protected ILink getLink(String moduleLinkIDValueEdit) throws LinkDoesNotExistsException {
        Optional<ModuleLink> linkOptional = moduleLinkRepo.findById(moduleLinkIDValueEdit);
        linksValidation(linkOptional);
        IModuleLink link = linkOptional.get();
        return link;
    }

    @Override
    protected ILinkDisplay createLinkDisplay(ILink link) {
        ILinkDisplay display = moduleLinkDisplayFactory.createModuleLinkDisplay((IModuleLink)link);
        return display;
    }

}
