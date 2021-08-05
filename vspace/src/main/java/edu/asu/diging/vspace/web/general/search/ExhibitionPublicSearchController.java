package edu.asu.diging.vspace.web.general.search;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashSet;

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

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleWithSpace;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SlideWithSpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;

@Controller
public class ExhibitionPublicSearchController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;
    
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
        model.addAttribute("spaceCount", publicSearchManager.getTotalSpaceCount(searchTerm));
        model.addAttribute("moduleCount", publicSearchManager.getTotalModuleCount(searchTerm));
        model.addAttribute("slideCount", publicSearchManager.getTotalSlideCount(searchTerm));
        model.addAttribute("slideTextCount", publicSearchManager.getTotalSlideTextCount(searchTerm));
//        model.addAttribute("activeTab", tab);
        return "exhibition/search/publicSearch";
    }
    
    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in space table and return the spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param model        This the object of Model attribute in spring spring MVC.
     * @param searchTerm   This is the search string which is being searched.
     */
    private void paginationForSpace(String spacePagenum, Model model, String searchTerm) {
        Page<Space> spacePage = publicSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
        model.addAttribute("spaceCurrentPageNumber", Integer.parseInt(spacePagenum));
        model.addAttribute("spaceTotalPages", spacePage.getTotalPages());
        HashSet<Space> spaceSet = new LinkedHashSet<>();
        spaceSet.addAll(spacePage.getContent());
        model.addAttribute("spaceSearchResults", spaceSet);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in module table and return the module corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param model         This the object of Model attribute in spring spring MVC.
     * @param searchTerm    This is the search string which is being searched.
     */
    private void paginationForModule(String modulePagenum, Model model, String searchTerm) {
        Page<Module> modulePage = publicSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
        model.addAttribute("moduleTotalPages", modulePage.getTotalPages());
        HashSet<ModuleWithSpace> moduleSet = new LinkedHashSet<>();
        
        //Adding space info for each module
        for(Module module : modulePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(module);
            if(moduleLink!=null) {
                ModuleWithSpace modWithSpace = new ModuleWithSpace();
                try {
                    BeanUtils.copyProperties(modWithSpace, module);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                modWithSpace.setSpaceId(moduleLink.getSpace().getId());
                moduleSet.add(modWithSpace);
            }
        }
        model.addAttribute("moduleSearchResults", moduleSet);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in slide table and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param model        This the object of Model attribute in spring spring MVC.
     * @param searchTerm   This is the search string which is being searched.
     */
    private void paginationForSlide(String slidePagenum, Model model, String searchTerm) {
        Page<Slide> slidePage = publicSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
        model.addAttribute("slideTotalPages", slidePage.getTotalPages());
        HashSet<Slide> slideSet = new LinkedHashSet<>();
        
       //Adding space info for each slide
        for(Slide slide : slidePage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                SlideWithSpace slideWithSpace = new SlideWithSpace();
                try {
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                slideSet.add(slideWithSpace);
            }
        }
        model.addAttribute("slideSearchResults", slideSet);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in ContentBlock table and return the slides
     * corresponding to the page number specified in the input
     * parameter(spacePagenum) whose text block contains the search string
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param model            This the object of Model attribute in spring spring
     *                         MVC.
     * @param searchTerm       This is the search string which is being searched.
     */
    private void paginationForSlideText(String slideTextPagenum, Model model, String searchTerm) {
        Page<Slide> slideTextPage = publicSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextTotalPages", slideTextPage.getTotalPages());
        HashSet<Slide> slideTextSet = new LinkedHashSet<>();
        
        //Adding space info for each slide
        for(Slide slide : slideTextPage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                SlideWithSpace slideWithSpace = new SlideWithSpace();
                try {
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                slideTextSet.add(slideWithSpace);
            }
        }
        model.addAttribute("slideTextSearchResults", slideTextSet);
    }
    
}