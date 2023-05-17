package edu.asu.diging.vspace.web.publicpages;

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

        SearchSpaceResults publicSearch = publicSearchManager.searchForSpace(spacePagenum, searchTerm);      
        return new ResponseEntity<SearchSpaceResults>(publicSearch, HttpStatus.OK);
    }

   
}
