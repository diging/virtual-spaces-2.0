package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.web.SearchController;

@Controller
public class StaffSearchController extends SearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search")
    public String searchInVspace(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        searchInSpaces(spacePagenum, model, searchTerm);        
        searchInModules(modulePagenum, model, searchTerm);
        searchInSlides(slidePagenum, model, searchTerm);
        searchInSlideTexts(slideTextPagenum, model, searchTerm);

        model.addAttribute("searchWord", searchTerm);

        return "/staff/search/staffSearch";
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
    private void searchInSpaces(String spacePagenum, Model model, String searchTerm) {
        Page<ISpace> spacePage = staffSearchManager.searchSpacesAndPaginate(searchTerm, Integer.parseInt(spacePagenum));
        updateModelWithSpaceSearchResult(model, spacePage, spacePagenum);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. The modulePagenum is starting from 1.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param model         This the object of Model attribute in spring MVC.
     * @param searchTerm    This is the search string which is being searched.
     */

    protected Page<IModule> searchInModules(String modulePagenum, Model model, String searchTerm) {   
        Page<IModule> modulePage = staffSearchManager.searchModulesAndPaginate(searchTerm, Integer.parseInt(modulePagenum));
        updateModelWithModuleSearchResult(modulePagenum, model, modulePage, modulePage.getContent() );
        return modulePage;

    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. The slidePagenum is starting from 1.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param model        This the object of Model attribute in spring MVC.
     * @param searchTerm   This is the search string which is being searched.
     */

    protected Page<ISlide>  searchInSlides(String slidePagenum, Model model, String searchTerm) {       
        Page<ISlide> slidePage  =  staffSearchManager.searchSlidesAndPaginate(searchTerm, Integer.parseInt(slidePagenum)) ;
        
        super.updateModelWithSlideSearchResults(slidePagenum, model, slidePage, slidePage.getContent());
        model.addAttribute("slideSearchResults", slidePage.getContent());
        return slidePage;
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. The slideTextPagenum is starting from 1.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param model            This the object of Model attribute in spring MVC.
     * 
     * @param searchTerm       This is the search string which is being searched.
     */

    protected Page<ISlide>  searchInSlideTexts(String slideTextPagenum, Model model, String searchTerm) {  
     
        Page<ISlide> slideTextPage = staffSearchManager.searchSlideTextsAndPaginate(searchTerm,  Integer.parseInt(slideTextPagenum));
        super.updateModelWithSlideTextSearchResults(slideTextPagenum, model, searchTerm, slideTextPage, slideTextPage.getContent())  ;      
        return slideTextPage;
    }
}
