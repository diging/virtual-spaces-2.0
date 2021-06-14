package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.ExternalLinkValue;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @Value("${page_size}")
    private int pageSize;

    @RequestMapping(value = "/staff/search")
    @GetMapping
    public String getAllSearchedElementsWithoutNum(@RequestParam(name = "searchText") String searchString) {
        return String.format("redirect:/staff/search/1?searchText=%s", searchString);
    }

    @RequestMapping(value = "/staff/search/{page}")
    @GetMapping
    public String getAllSearchedElements(@PathVariable(required = false) String page, Model model,
            @RequestParam(name = "searchText") String searchString) {
        HashSet<IVSpaceElement> elementSet = staffSearchManager.getAllSearchedElements(searchString);
        List<IVSpaceElement> elementList = new ArrayList<>(elementSet);
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = validatePageNumber(Integer.parseInt(page), elementList.size());
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex < elementSet.size()) {
            elementList = elementList.subList(startIndex, endIndex);
        } else {
            if (startIndex < elementSet.size()) {
                elementList = elementList.subList(startIndex, elementSet.size());
            }
        }
        model.addAttribute("totalPages", getTotalPages(elementSet.size()));
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("searchWord", searchString);
        model.addAttribute("conIndex", new ExternalLinkValue("0"));
        model.addAttribute("searchResults", elementList);
        model.addAttribute("resultCount", elementList.size());
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
