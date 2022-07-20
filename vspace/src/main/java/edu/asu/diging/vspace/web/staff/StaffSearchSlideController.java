package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

        return new ResponseEntity<SearchSlideResults>(staffSearchManager.searchForSlide(slidePagenum, searchTerm), HttpStatus.OK);
    }

}
