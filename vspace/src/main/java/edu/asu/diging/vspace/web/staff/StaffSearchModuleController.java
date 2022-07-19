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

        List<IModule> moduleList = paginationForModule(modulePagenum, searchTerm);        
        SearchModuleResults staffSearch = staffSearchManager.getSearchModuleResults(moduleList);
        return new ResponseEntity<SearchModuleResults>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    private List<IModule> paginationForModule(String modulePagenum, String searchTerm) {
        Page<IModule> modulePage = staffSearchManager.paginationInModules(searchTerm, Integer.parseInt(modulePagenum));
        return modulePage.getContent();
    }

}
