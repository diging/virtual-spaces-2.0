package edu.asu.diging.vspace.web.staff;

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

import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.StaffSearchSpace;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchSpaceController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search/space")
    public ResponseEntity<StaffSearchSpace> searchInVspace(
            @RequestParam(value = "spacePagenum", required = false, defaultValue = "1") String spacePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) throws JsonProcessingException {

        List<Space> spaceList = paginationForSpace(spacePagenum, searchTerm);
        StaffSearchSpace staffSearch = new StaffSearchSpace();
        staffSearch.setSpace(spaceList);
        return new ResponseEntity<StaffSearchSpace>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in space table and return the spaces corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param spacePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private List<Space> paginationForSpace(String spacePagenum, String searchTerm) {
        Page<Space> spacePage = staffSearchManager.searchInSpaces(searchTerm, Integer.parseInt(spacePagenum));
        return spacePage.getContent();
    }
}
