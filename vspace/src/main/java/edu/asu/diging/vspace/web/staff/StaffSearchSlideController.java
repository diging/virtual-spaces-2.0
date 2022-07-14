package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;

@Controller
public class StaffSearchSlideController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search/slide")
    public ResponseEntity<SearchSlideResults> searchInVspace(
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideList = paginationForSlide(slidePagenum, searchTerm);
        SearchSlideResults staffSearch  = staffSearchManager.getSearchSlideResults(slideList);
        return new ResponseEntity<SearchSlideResults>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlide(String slidePagenum, String searchTerm) {
        Page<ISlide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        return slidePage.getContent();
    }
}
