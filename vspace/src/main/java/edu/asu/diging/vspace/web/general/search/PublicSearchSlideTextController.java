package edu.asu.diging.vspace.web.general.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.StaffSearch;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class PublicSearchSlideTextController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/exhibit/search/slideText")
    public ResponseEntity<StaffSearch> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        HashSet<Slide> slideTextSet = paginationForSlideText(slideTextPagenum, searchTerm);
        StaffSearch staffSearch = new StaffSearch();
        staffSearch.setSlideTextSet(slideTextSet);
        
        Map<String, String> slideTextFirstImage = new HashMap<>();
        
        for (Slide slide : slideTextSet) {
            
            String slideFirstImageId = null;
            
            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImageId = slide.getFirstImageBlock().getImage().getId();
            }
            slideTextFirstImage.put(slide.getId(), slideFirstImageId);
            staffSearch.setSlideTextFirstImage(slideTextFirstImage);
        }
        return new ResponseEntity<StaffSearch>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in ContentBlock table and return the slides
     * corresponding to the page number specified in the input
     * parameter(spacePagenum) whose text block contains the search string
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private HashSet<Slide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<Slide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        HashSet<Slide> slideTextSet = new LinkedHashSet<>();
        slideTextSet.addAll(slideTextPage.getContent());
        return slideTextSet;
    }
}
