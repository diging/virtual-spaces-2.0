package edu.asu.diging.vspace.web.general.search;

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
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleWithSpace;
import edu.asu.diging.vspace.web.SearchController;


@Controller
public class PublicSearchController extends SearchController {
    
    @Autowired
    private IPublicSearchManager publicSearchManager;
  
    
    @RequestMapping(value = "/exhibit/search", method=RequestMethod.GET)
    public String getAllSearchedElements(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm,
            @RequestParam(value = "tab", defaultValue = "module") String tab) {

        searchInSpaces(spacePagenum, model, searchTerm);        
        searchInModules(modulePagenum, model, searchTerm);
        searchInSlides(slidePagenum, model, searchTerm);
        searchInSlideTexts(slideTextPagenum, model, searchTerm);
        
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
    public Page<ISpace> searchInSpaces(String spacePagenum, Model model, String searchTerm) {
        Page<ISpace> spacePage = publicSearchManager.searchSpacesAndPaginate(searchTerm, Integer.parseInt(spacePagenum));
        updateModelWithSpaceSearchResult(model, spacePage, spacePagenum);
        return spacePage;
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


    public Page<IModule> searchInModules(String modulePagenum, Model model, String searchTerm) {
        Page<IModule> modulePage  = publicSearchManager.searchModulesAndPaginate(searchTerm, Integer.parseInt(modulePagenum));
        updateModelWithModuleSearchResult(modulePagenum, model, modulePage, modulePage.getContent());
        return modulePage;
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
    public Page<ISlide> searchInSlides(String slidePagenum, Model model, String searchTerm) {     
        Page<ISlide> slidePage = publicSearchManager.searchSlidesAndPaginate( searchTerm, Integer.parseInt(slidePagenum));        
        updateModelWithSlideSearchResults(slidePagenum, model, slidePage, slidePage.getContent());
        return slidePage;
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
    public Page<ISlide> searchInSlideTexts(String slideTextPagenum, Model model, String searchTerm) {       
        Page<ISlide> slideTextPage = publicSearchManager.searchSlideTextsAndPaginate(searchTerm, Integer.parseInt(slideTextPagenum));
        updateModelWithSlideTextSearchResults(slideTextPagenum, model, searchTerm, slideTextPage, slideTextPage.getContent());
        return slideTextPage;
    }

    
}