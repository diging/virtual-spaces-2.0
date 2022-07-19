package edu.asu.diging.vspace.web.staff;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;

@Controller
public class StaffSearchSlideTextController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search/slideText")
    public ResponseEntity<SearchSlideTextBlockResults> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideTextList = paginationForSlideText(slideTextPagenum, searchTerm);

        SearchSlideTextBlockResults staffSearch  = staffSearchManager.getSearchSlideTextBlockResults(slideTextList, searchTerm);
        return new ResponseEntity<SearchSlideTextBlockResults>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<ISlide> slideTextPage = staffSearchManager.paginationInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        return slideTextPage.getContent();
    }
}
