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

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchSpaceResults;

@Controller
public class PublicSearchSpaceController {

    @Autowired
    private IPublicSearchManager publicSearchManager;

    @RequestMapping(value = "/exhibit/search/space")
    public ResponseEntity<SearchSpaceResults> searchInVspace(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) throws JsonProcessingException {

        SearchSpaceResults publicSearch = paginationForSpace(spacePagenum, searchTerm);      
        return new ResponseEntity<SearchSpaceResults>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the published spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private SearchSpaceResults paginationForSpace(String spacePagenum, String searchTerm) {
        Page<ISpace> spacePage = publicSearchManager.paginationInSpaces(searchTerm, Integer.parseInt(spacePagenum));     
        return publicSearchManager.getSearchSpaceResults(spacePage.getContent());
    }
  
}
