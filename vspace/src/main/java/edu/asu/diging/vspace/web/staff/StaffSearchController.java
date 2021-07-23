package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;
import java.util.LinkedHashSet;

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
        Page<Space> spacePage = staffSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
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
        Page<Module> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
        model.addAttribute("moduleTotalPages", modulePage.getTotalPages());
        HashSet<Module> moduleSet = new LinkedHashSet<>();
        moduleSet.addAll(modulePage.getContent());
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
        Page<Slide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
        model.addAttribute("slideTotalPages", slidePage.getTotalPages());
        HashSet<Slide> slideSet = new LinkedHashSet<>();
        slideSet.addAll(slidePage.getContent());
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
        Page<Slide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextTotalPages", slideTextPage.getTotalPages());
        HashSet<Slide> slideTextSet = new LinkedHashSet<>();
        slideTextSet.addAll(slideTextPage.getContent());
        model.addAttribute("slideTextSearchResults", slideTextSet);
    }
}
