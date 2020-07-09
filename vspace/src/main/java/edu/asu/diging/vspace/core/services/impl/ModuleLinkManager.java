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
import edu.asu.diging.vspace.core.exception.LinkDoesNotExistsException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IModuleLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.IModuleLinkFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.IModuleLink;
import edu.asu.diging.vspace.core.model.ISpace;
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
    public IModuleLinkDisplay createLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, DisplayType displayType, byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, ImageCouldNotBeStoredException, SpaceDoesNotExistException {
        ISpace source = spaceManager.getSpace(id);
        if (source == null) {
            throw new SpaceDoesNotExistException();
        }

        IModule target = moduleManager.getModule(linkedModuleId);
        IModuleLink link = moduleLinkFactory.createModuleLink(title, source);
        link.setModule(target);
        moduleLinkRepo.save((ModuleLink) link);

        IModuleLinkDisplay display = moduleLinkDisplayFactory.createModuleLinkDisplay(link);
        display.setPositionX(positionX);
        display.setPositionY(positionY);
        display.setRotation(rotation);
        display.setType(displayType != null ? displayType : DisplayType.MODULE);

        moduleLinkDisplayRepo.save((ModuleLinkDisplay) display);
        return display;
    }

    @Override
    public List<ModuleLinkDisplay> getLinkDisplays(String spaceId) {
        return new ArrayList<>(moduleLinkDisplayRepo.findModuleLinkDisplaysForSpace(spaceId));
    }

    @Override
    public IModuleLinkDisplay updateLink(String title, String id, float positionX, float positionY, int rotation,
            String linkedModuleId, String moduleLinkLabel, String linkId, String moduleLinkDisplayId, DisplayType displayType,
            byte[] linkImage, String imageFilename)
                    throws SpaceDoesNotExistException, LinkDoesNotExistsException, ImageCouldNotBeStoredException {

        spaceValidation(id);

        Optional<ModuleLink> linkOptional = moduleLinkRepo.findById(linkId);
        Optional<ModuleLinkDisplay> moduleLinkOptional = moduleLinkDisplayRepo.findById(moduleLinkDisplayId);

        linksValidation(linkOptional, moduleLinkOptional);

        IModuleLink link = linkOptional.get();
        IModuleLinkDisplay display = moduleLinkOptional.get();

        link.setName(title);
        IModule module = moduleManager.getModule(linkedModuleId);
        link.setModule(module);

        populateDisplay((ILinkDisplay)display,positionX,positionY,rotation,displayType,null,null);

        moduleLinkRepo.save((ModuleLink) link);
        moduleLinkDisplayRepo.save((ModuleLinkDisplay) display);

        return display;
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

}
