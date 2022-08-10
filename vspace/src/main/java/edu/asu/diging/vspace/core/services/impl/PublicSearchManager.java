package edu.asu.diging.vspace.core.services.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;
import edu.asu.diging.vspace.core.services.impl.model.SearchSpaceResults;
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
    
    
    public List<ISlide> updateSlidePageWithSpaceInfo(Page<ISlide> slidePage) {

        List<ISlide> slideList = new ArrayList<>();

        //Adding space info for each slide
        for(ISlide slide : slidePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                SlideWithSpace slideWithSpace = new SlideWithSpace();
                try {
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                slideWithSpace.setStartSequenceId(slide.getModule().getStartSequence().getId());
                slideList.add(slideWithSpace);
            }
        }
        return slideList;
    }

    public List<IModule> updateModulePageWithSpaceInfo(Page<IModule> modulePage) {
        List<IModule> moduleList = new ArrayList<>();

        //Adding space info for each module
        for(IModule module : modulePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(module);
            if(moduleLink!=null) {
                ModuleWithSpace modWithSpace = new ModuleWithSpace();
                try {
                    BeanUtils.copyProperties(modWithSpace, module);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                modWithSpace.setSpaceId(moduleLink.getSpace().getId());
                moduleList.add(modWithSpace);
            }
        }

        return moduleList;
    }


    @Override
    public List<ISlide> updateSlideTextPageWithSpaceInfo(Page<ISlide> slideTextPage) {

        List<ISlide> slideTextList = new ArrayList<>();

        //Adding space info for each slide
        for(ISlide slide : slideTextPage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                SlideWithSpace slideWithSpace = new SlideWithSpace();
                try {
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                slideWithSpace.setStartSequenceId(slide.getModule().getStartSequence().getId());
                slideTextList.add(slideWithSpace);
            }
        }
        return slideTextList;
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
        return textContentBlockRepo.findWithNameOrDescriptionLinkedToSpace(requestedPageForSlideText, searchTerm);
    }


    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the published spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    @Override
    public SearchSpaceResults searchForSpace(String spacePagenum, String searchTerm) {
        Page<ISpace> spacePage = searchSpacesAndPaginate(searchTerm, Integer.parseInt(spacePagenum));     
        return getSearchSpaceResults(spacePage.getContent());
    }


    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. This also filters modules which are linked to the spaces.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    @Override
    public SearchModuleResults searchForModule(String modulePagenum, String searchTerm) {
        Page<IModule> modulePage = searchModulesAndPaginate(searchTerm, Integer.parseInt(modulePagenum));    
        List<IModule> moduleList =  updateModulePageWithSpaceInfo(modulePage);
        return getSearchModuleResults(moduleList);
        
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string. This also filters Slides from modules 
     * which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    @Override
    public SearchSlideResults searchForSlide(String slidePagenum, String searchTerm) {
        Page<ISlide> slidePage = searchSlidesAndPaginate(searchTerm, Integer.parseInt(slidePagenum));      
        List<ISlide> slideList = updateSlidePageWithSpaceInfo(slidePage);       
        return getSearchSlideResults(slideList);     
    }
   

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    @Override
    public SearchSlideTextBlockResults searchForSlideText(String slideTextPagenum, String searchTerm) {
        Page<ISlide> slideTextPage = searchSlideTextsAndPaginate(searchTerm,
                Integer.parseInt(slideTextPagenum));
        List<ISlide> slideTextList = updateSlideTextPageWithSpaceInfo(slideTextPage);
        return getSearchSlideTextBlockResults(slideTextList, searchTerm);     
        
    }


    
}