package edu.asu.diging.vspace.core.services.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;
import edu.asu.diging.vspace.core.services.impl.model.SlideWithSpace;

@Service
public class PublicSearchManager extends SearchManager implements IPublicSearchManager{
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private TextContentBlockRepository textContentBlockRepo;
    
    @Override
    protected Page<ISpace> searchSpaces(Pageable requestedPageForSpace, String searchTerm) {
        return spaceManager.findBySpaceStatusAndNameOrDescription(requestedPageForSpace, SpaceStatus.PUBLISHED, searchTerm);
    }   
    
    @Override
    protected Page<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage) {

        List<ISlide> slideList = new ArrayList<>();

        //Adding space info for each slide
        for(ISlide slide : slidePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                
                try {
                    SlideWithSpace slideWithSpace = new SlideWithSpace();
                    slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                    slideWithSpace.setStartSequenceId(slide.getModule().getStartSequence().getId());
                    slideList.add(slideWithSpace);
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create slideWithSpace.", e);
                }               
            }
        }       
        return new PageImpl<>(slideList, slidePage.getPageable(), slidePage.getTotalElements());
    }

    @Override
    protected Page<IModule> updateModulePageWithSpaceInfo(Page<IModule> modulePage) {
        List<IModule> moduleList = new ArrayList<>();

        //Adding space info for each module
        for(IModule module : modulePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(module);
            if(moduleLink!=null) {
                try {
                    ModuleWithSpace modWithSpace = new ModuleWithSpace();
                    modWithSpace.setSpaceId(moduleLink.getSpace().getId());
                    moduleList.add(modWithSpace);
                    BeanUtils.copyProperties(modWithSpace, module);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                
            }
        }
        return new PageImpl<>(moduleList, modulePage.getPageable(), modulePage.getTotalElements());
    }

    @Override
    protected Page<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage) {

        List<ISlide> slideTextList = new ArrayList<>();

        //Adding space info for each slide
        for(ISlide slide : slideTextPage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                try {
                    SlideWithSpace slideWithSpace = new SlideWithSpace();
                    slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                    slideWithSpace.setStartSequenceId(slide.getModule().getStartSequence().getId());
                    slideTextList.add(slideWithSpace);
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create slideTextWithSpace.", e);
                }
            }
        }
        return new PageImpl<>(slideTextList, slideTextPage.getPageable(), slideTextPage.getTotalElements());
    }

    @Override
    protected Page<IModule> searchModules(Pageable requestedPageForModule, String searchTerm) {
        Page<IModule> modulePage =  moduleManager.findByNameOrDescriptionLinkedToSpace(requestedPageForModule, searchTerm);
        updateModulePageWithSpaceInfo(modulePage);
        return modulePage;        
    }

    @Override
    protected Page<ISlide> searchSlides(Pageable requestedPageForSlide, String searchTerm) { 
        return slideManager.findByNameOrDescriptionLinkedToSpace(requestedPageForSlide, searchTerm);      
    }

    @Override
    protected Page<ISlide> searchSlideTexts(Pageable requestedPageForSlideText, String searchTerm) {
        return textContentBlockRepo.searchByNameOrDescription(requestedPageForSlideText, searchTerm, SpaceStatus.PUBLISHED, ModuleStatus.PUBLISHED);
    }
    
}