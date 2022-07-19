package edu.asu.diging.vspace.web.general.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideResults;

@Controller
public class PublicSearchSlideController {
        
    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @RequestMapping(value = "/exhibit/search/slide")
    public ResponseEntity<SearchSlideResults> searchInVspace(
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideList = paginationForSlide(slidePagenum, searchTerm);        
        SearchSlideResults publicSearch =  publicSearchManager.getSearchSlideResults(slideList);        
        return new ResponseEntity<SearchSlideResults>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string. This also filters Slides from modules 
     * which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlide(String slidePagenum, String searchTerm) {
        Page<ISlide> slidePage = publicSearchManager.paginationInSlides(searchTerm, Integer.parseInt(slidePagenum));      
        List<ISlide> slideList = publicSearchManager.updateSlidePageWithSpaceInfo(slidePage);      
        return slideList;
    }
}
