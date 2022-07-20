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

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISearchManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.SearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;

@Controller
public class StaffSearchModuleController {

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @RequestMapping(value = "/staff/search/module")
    public ResponseEntity<SearchModuleResults> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        return new ResponseEntity<SearchModuleResults>(staffSearchManager.searchForModule(modulePagenum, searchTerm), HttpStatus.OK);
    }


}
