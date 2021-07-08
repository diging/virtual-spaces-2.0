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
    public String getAllSearchedElements(@RequestParam(value = "page", defaultValue = "1") String page, Model model,
            @RequestParam(name = "searchText") String searchString, @RequestParam(value = "tab", defaultValue = "module") String tab) {

        paginationForSpace(page, model, searchString, tab);

        paginationForModule(page, model, searchString, tab);

        paginationForSlide(page, model, searchString, tab);

        paginationForSlideText(page, model, searchString, tab);
        
        model.addAttribute("searchWord", searchString);
        model.addAttribute("conIndex", new StringBuilder());
        model.addAttribute("activeTab", tab);
        return "/staff/search/staffSearch";
    }

    /**
     * This method is used for pagination of all searched string that belong to space table
     * 
     * @param page ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     * @param tab ---> This is the current tab name
     */
    private void paginationForSpace(String page, Model model, String searchString, String tab) {
        long totalSpacePage = staffSearchManager.getCountOfSearchedSpace(searchString);
        StringBuilder strSpacePageNo = new StringBuilder();
        HashSet<IVSpaceElement> spaceSet = staffSearchManager.searchSpaces(searchString,Integer.parseInt(page),totalSpacePage,tab,strSpacePageNo);
        model.addAttribute("spaceCurrentPageNumber", Integer.parseInt(strSpacePageNo.toString()));
        model.addAttribute("spaceTotalPages", staffSearchManager.getTotalPages(totalSpacePage));
        model.addAttribute("spaceSearchResults", spaceSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to module table
     * 
     * @param page ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     * @param tab ---> This is the current tab name
     */
    private void paginationForModule(String page, Model model, String searchString, String tab) {
        long totalModulePage = staffSearchManager.getCountOfSearchedModule(searchString);
        StringBuilder strModulePageNo = new StringBuilder();
        HashSet<IVSpaceElement> moduleSet = staffSearchManager.searchModules(searchString, Integer.parseInt(page),totalModulePage,tab,strModulePageNo);
        model.addAttribute("moduleCurrentPageNumber", Integer.parseInt(strModulePageNo.toString()));
        model.addAttribute("moduleTotalPages", staffSearchManager.getTotalPages(totalModulePage));
        model.addAttribute("moduleSearchResults", moduleSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to slide table
     * 
     * @param page ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     * @param tab ---> This is the current tab name
     */
    private void paginationForSlide(String page, Model model, String searchString, String tab) {
        long totalSlidePage = staffSearchManager.getCountOfSearchedSlide(searchString);
        StringBuilder strSlidePageNo = new StringBuilder();
        HashSet<IVSpaceElement> slideSet = staffSearchManager.searchSlides(searchString, Integer.parseInt(page),totalSlidePage,tab,strSlidePageNo);
        model.addAttribute("slideCurrentPageNumber", Integer.parseInt(strSlidePageNo.toString()));
        model.addAttribute("slideTotalPages", staffSearchManager.getTotalPages(totalSlidePage));
        model.addAttribute("slideSearchResults", slideSet);
    }
    
    /**
     * This method is used for pagination of all searched string that belong to text blocks of slide
     * 
     * @param page ---> current page number sent as request parameter in the URL.
     * @param model ---> This the object of Model attribute in spring spring MVC.
     * @param searchString ---> This is the search string which is being searched.
     * @param tab ---> This is the current tab name
     */
    private void paginationForSlideText(String page, Model model, String searchString, String tab) {
        long totalSlideTextPage = staffSearchManager.getCountOfSearchedSlideText(searchString);
        StringBuilder strSlideTextPageNo = new StringBuilder();
        HashSet<IVSpaceElement> slideTextSet = staffSearchManager.searchSlideTexts(searchString, Integer.parseInt(page),totalSlideTextPage,tab,strSlideTextPageNo);
        model.addAttribute("slideTextCurrentPageNumber", Integer.parseInt(strSlideTextPageNo.toString()));
        model.addAttribute("slideTextTotalPages", staffSearchManager.getTotalPages(totalSlideTextPage));
        model.addAttribute("slideTextSearchResults", slideTextSet);
    }
}
