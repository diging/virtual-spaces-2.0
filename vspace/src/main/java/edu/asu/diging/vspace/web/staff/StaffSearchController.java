package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search")
    public String searchInVspace(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        paginationForSpace(spacePagenum, model, searchTerm);

        paginationForModule(modulePagenum, model, searchTerm);

        paginationForSlide(slidePagenum, model, searchTerm);

        paginationForSlideText(slideTextPagenum, model, searchTerm);

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
    private void paginationForSpace(String spacePagenum, Model model, String searchTerm) {
        Page<Space> spacePage = staffSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
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
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param model         This the object of Model attribute in spring MVC.
     * @param searchTerm    This is the search string which is being searched.
     */
    private void paginationForModule(String modulePagenum, Model model, String searchTerm) {
        Page<Module> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
        model.addAttribute("moduleTotalPages", modulePage.getTotalPages());
        model.addAttribute("moduleSearchResults", modulePage.getContent());
        model.addAttribute("moduleCount", modulePage.getTotalElements());
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
    private void paginationForSlide(String slidePagenum, Model model, String searchTerm) {
        Page<Slide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
        model.addAttribute("slideTotalPages", slidePage.getTotalPages());
        model.addAttribute("slideSearchResults", slidePage.getContent());
        model.addAttribute("slideCount", slidePage.getTotalElements());
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
    private void paginationForSlideText(String slideTextPagenum, Model model, String searchTerm) {
        Page<Slide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextTotalPages", slideTextPage.getTotalPages());
        model.addAttribute("slideTextSearchResults", slideTextPage.getContent());
        model.addAttribute("slideTextCount", slideTextPage.getTotalElements());
    }
}
