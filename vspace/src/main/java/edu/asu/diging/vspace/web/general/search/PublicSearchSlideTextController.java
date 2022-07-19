package edu.asu.diging.vspace.web.general.search;

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
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchSlideTextBlockResults;

@Controller
public class PublicSearchSlideTextController {

    @Autowired
    private IPublicSearchManager publicSearchManager;
    
    @RequestMapping(value = "/exhibit/search/slideText")
    public ResponseEntity<SearchSlideTextBlockResults> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideTextList = paginationForSlideText(slideTextPagenum, searchTerm);       
        SearchSlideTextBlockResults publicSearch  = publicSearchManager.getSearchSlideTextBlockResults(slideTextList, searchTerm);       
        return new ResponseEntity<SearchSlideTextBlockResults>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<ISlide> slideTextPage = publicSearchManager.paginationInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        return publicSearchManager.updateSlideTextPageWithSpaceInfo(slideTextPage);
    }
}
