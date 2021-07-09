package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search")
    public String searchInVspace(@RequestParam(value = "spacePagenum",required=false, defaultValue = "1") String spacePagenum, 
            @RequestParam(value = "modulePagenum",required=false, defaultValue = "1") String modulePagenum,
            @RequestParam(value = "slidePagenum",required=false, defaultValue = "1") String slidePagenum,
            @RequestParam(value = "slideTextPagenum",required=false, defaultValue = "1") String slideTextPagenum,Model model,
            @RequestParam(name = "searchText") String searchString, @RequestParam(value = "tab", defaultValue = "module") String tab) {

        paginationForSpace(spacePagenum, model, searchString);

        paginationForModule(modulePagenum, model, searchString);

        paginationForSlide(slidePagenum, model, searchString);

        paginationForSlideText(slideTextPagenum, model, searchString);
        
        model.addAttribute("searchWord", searchString);
        model.addAttribute("activeTab", tab);
        return "/staff/search/staffSearch";
    }

    /**
     * This method is used for pagination of all searched string that belong to space table
     * 
     * @param spacePagenum ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     */
    private void paginationForSpace(String spacePagenum, Model model, String searchString) {
        HashSet<IVSpaceElement> spaceSet = staffSearchManager.searchInSpaces(searchString,Integer.parseInt(spacePagenum));
        model.addAttribute("spaceCurrentPageNumber",Integer.parseInt(spacePagenum));
        model.addAttribute("spaceTotalPages", staffSearchManager.getTotalSpacePages(searchString));
        model.addAttribute("spaceSearchResults", spaceSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to module table
     * 
     * @param modulePagenum ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     */
    private void paginationForModule(String modulePagenum, Model model, String searchString) {
        HashSet<IVSpaceElement> moduleSet = staffSearchManager.searchInModules(searchString, Integer.parseInt(modulePagenum));
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(modulePagenum));
        model.addAttribute("moduleTotalPages", staffSearchManager.getTotalModulePages(searchString));
        model.addAttribute("moduleSearchResults", moduleSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to slide table
     * 
     * @param slidePagenum ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     */
    private void paginationForSlide(String slidePagenum, Model model, String searchString) {
        HashSet<IVSpaceElement> slideSet = staffSearchManager.searchInSlides(searchString, Integer.parseInt(slidePagenum));
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(slidePagenum));
        model.addAttribute("slideTotalPages", staffSearchManager.getTotalSlidePages(searchString));
        model.addAttribute("slideSearchResults", slideSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to text blocks of slide
     * 
     * @param slideTextPagenum ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     */
    private void paginationForSlideText(String slideTextPagenum, Model model, String searchString) {
        HashSet<IVSpaceElement> slideTextSet = staffSearchManager.searchInSlideTexts(searchString, Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(slideTextPagenum));
        model.addAttribute("slideTextTotalPages", staffSearchManager.getTotalSlideTextPages(searchString));
        model.addAttribute("slideTextSearchResults", slideTextSet);
    }
}
