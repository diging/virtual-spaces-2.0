package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    // @Value("${page_size}")
    private int pageSize = 3;

    @RequestMapping(value = "/staff/search")
    @GetMapping
    public String getAllSearchedElementsWithoutNum(@RequestParam(name = "searchText") String searchString) {
        return String.format("redirect:/staff/searchPage?page=%s&searchText=%s", "1", searchString);
    }

    @RequestMapping(value = "/staff/searchPage")
    @GetMapping
    public String getAllSearchedElements(@RequestParam(value = "page", required = false) String page, Model model,
            @RequestParam(name = "searchText") String searchString,
            @RequestParam(value = "tab", required = false) String tab) {

        int spacePageNo;
        int modulePageNo;
        int slidePageNo;
        int slideTextPageNo;
        
        if (tab == null || (tab != null && tab.trim().isEmpty())) {
            tab = "module";
        }
        page = StringUtils.isEmpty(page) ? "1" : page;

        int totalSpacePage = staffSearchManager.getCountOfSearchedSpace(searchString);
        try {
            spacePageNo = validatePageNumber(Integer.parseInt(page), totalSpacePage);
        } catch (NumberFormatException numberFormatException) {
            spacePageNo = 1;
        }

        int totalModulePage = staffSearchManager.getCountOfSearchedModule(searchString);
        try {
            modulePageNo = validatePageNumber(Integer.parseInt(page), totalModulePage);
        } catch (NumberFormatException numberFormatException) {
            modulePageNo = 1;
        }

        int totalSlidePage = staffSearchManager.getCountOfSearchedSlide(searchString);
        try {
            slidePageNo = validatePageNumber(Integer.parseInt(page), totalSlidePage);
        } catch (NumberFormatException numberFormatException) {
            slidePageNo = 1;
        }

        int totalSlideTextPage = staffSearchManager.getCountOfSearchedSlideText(searchString);
        try {
            slideTextPageNo = validatePageNumber(Integer.parseInt(page), totalSlideTextPage);
        } catch (NumberFormatException numberFormatException) {
            slideTextPageNo = 1;
        }

        // Reseting the page number of all other tabs apart from current tab to 1.
        if (tab.trim().equals("space")) {
            modulePageNo = 1;
            slidePageNo = 1;
            slideTextPageNo = 1;
        }
        if (tab.trim().equals("module")) {
            spacePageNo = 1;
            slidePageNo = 1;
            slideTextPageNo = 1;
        }
        if (tab.trim().equals("slide")) {
            spacePageNo = 1;
            modulePageNo = 1;
            slideTextPageNo = 1;
        }
        if (tab.trim().equals("slideText")) {
            spacePageNo = 1;
            modulePageNo = 1;
            slidePageNo = 1;
        }

        Pageable requestedPageForSpace = PageRequest.of(spacePageNo - 1, pageSize);
        HashSet<IVSpaceElement> spaceSet = staffSearchManager.getAllSearchedSpaces(searchString, requestedPageForSpace);

        Pageable requestedPageForModule = PageRequest.of(modulePageNo - 1, pageSize);
        HashSet<IVSpaceElement> moduleSet = staffSearchManager.getAllSearchedModules(searchString,
                requestedPageForModule);

        Pageable requestedPageForSlide = PageRequest.of(slidePageNo - 1, pageSize);
        HashSet<IVSpaceElement> slideSet = staffSearchManager.getAllSearchedSlides(searchString, requestedPageForSlide);

        Pageable requestedPageForSlideText = PageRequest.of(slideTextPageNo - 1, pageSize);
        HashSet<IVSpaceElement> slideTextSet = staffSearchManager.getAllSearchedSlideTexts(searchString,
                requestedPageForSlideText);

        model.addAttribute("spaceTotalPages", getTotalPages(totalSpacePage));
        model.addAttribute("moduleTotalPages", getTotalPages(totalModulePage));
        model.addAttribute("slideTotalPages", getTotalPages(totalSlidePage));
        model.addAttribute("slideTextTotalPages", getTotalPages(totalSlideTextPage));
        model.addAttribute("spaceCurrentPageNumber", spacePageNo);
        model.addAttribute("moduleCurrentPageNumber", modulePageNo);
        model.addAttribute("slideCurrentPageNumber", slidePageNo);
        model.addAttribute("slideTextCurrentPageNumber", slideTextPageNo);
        model.addAttribute("searchWord", searchString);
        model.addAttribute("conIndex", new StringBuilder());
        model.addAttribute("spaceSearchResults", spaceSet);
        model.addAttribute("moduleSearchResults", moduleSet);
        model.addAttribute("slideSearchResults", slideSet);
        model.addAttribute("slideTextSearchResults", slideTextSet);

        model.addAttribute("activeTab", tab);
        return "/staff/search/staffSearch";
    }

    private int validatePageNumber(int pageNo, long totalPages) {
        if (pageNo < 1) {
            return 1;
        } else if (pageNo > totalPages) {
            return (totalPages == 0) ? 1 : (int) totalPages;
        }
        return pageNo;
    }

    long getTotalPages(long count) {
        return (count % pageSize == 0) ? count / pageSize : (count / pageSize) + 1;
    }

}
