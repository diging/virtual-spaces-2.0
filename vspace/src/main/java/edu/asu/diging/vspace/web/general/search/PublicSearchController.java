package edu.asu.diging.vspace.web.general.search;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;
import edu.asu.diging.vspace.core.services.impl.model.SlideWithSpace;

@Controller
public class PublicSearchController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @RequestMapping(value = "/exhibit/search", method=RequestMethod.GET)
    public String getAllSearchedElements(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm,
            @RequestParam(value = "tab", defaultValue = "module") String tab) {

        paginationForSpace(spacePagenum, model, searchTerm);
        paginationForModule(modulePagenum, model, searchTerm);
        paginationForSlide(slidePagenum, model, searchTerm);
        paginationForSlideText(slideTextPagenum, model, searchTerm);
        model.addAttribute("searchWord", searchTerm);
        return "exhibition/search/publicSearch";
    }
    
    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the spaces corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. The spacePagenum is starting from 1. 
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param model        This the object of Model attribute in spring MVC.
     * @param searchTerm   This is the search string which is being searched.
     */
    private void paginationForSpace(String spacePagenum, Model model, String searchTerm) {
        Page<ISpace> spacePage = publicSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
        model.addAttribute("spaceCurrentPageNumber", Integer.parseInt(spacePagenum));
        model.addAttribute("spaceTotalPages", spacePage.getTotalPages());
        model.addAttribute("spaceSearchResults", spacePage.getContent());
        model.addAttribute("spaceCount", spacePage.getTotalElements());
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. The modulePagenum is starting from 1. 
     * This also filters modules which are linked to the spaces.
     * 
     * @param modulePagenum current page number sent as request parameter in the URL.
     * @param model         This the object of Model attribute in spring MVC.
     * @param searchTerm    This is the search string which is being searched.
     */
    private void paginationForModule(String modulePagenum, Model model, String searchTerm) {
        Page<IModule> modulePage = publicSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
        model.addAttribute("moduleTotalPages", modulePage.getTotalPages());
        List<ModuleWithSpace> moduleList = new ArrayList<>();
        
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
        model.addAttribute("moduleSearchResults", moduleList);
        model.addAttribute("moduleCount", modulePage.getTotalElements());
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. The slidePagenum is starting from 1. 
     * This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param model        This the object of Model attribute in spring MVC.
     * @param searchTerm   This is the search string which is being searched.
     */
    private void paginationForSlide(String slidePagenum, Model model, String searchTerm) {
        Page<ISlide> slidePage = publicSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
        model.addAttribute("slideTotalPages", slidePage.getTotalPages());
        List<ISlide> slideList = new ArrayList<>();
        Set<String> slideSet = slideManager.getAllSlidesFromStartSequences();
        
       //Adding space info for each slide
        for(ISlide slide : slidePage.getContent()) {
            if(slideSet.contains(slide.getId())) {
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
        }
        model.addAttribute("slideSearchResults", slideList);
        model.addAttribute("slideCount", slidePage.getTotalElements());
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. The slideTextPagenum is starting from 1.
     * This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param model            This the object of Model attribute in spring MVC.
     * @param searchTerm       This is the search string which is being searched.
     */
    private void paginationForSlideText(String slideTextPagenum, Model model, String searchTerm) {
        Page<ISlide> slideTextPage = publicSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextTotalPages", slideTextPage.getTotalPages());
        List<Slide> slideTextList = new ArrayList<>();
        Set<String> slideSet = slideManager.getAllSlidesFromStartSequences();
        
        //Adding space info for each slide
        for(ISlide slide : slideTextPage.getContent()) {
            if(slideSet.contains((String)slide.getId())) {
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
        }
        model.addAttribute("slideTextSearchResults", slideTextList);
        model.addAttribute("slideTextCount", slideTextPage.getTotalElements());
    }
    
}