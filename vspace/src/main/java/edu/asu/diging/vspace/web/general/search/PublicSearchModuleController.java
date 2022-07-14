package edu.asu.diging.vspace.web.general.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IPublicSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.SearchModuleResults;

@Controller
public class PublicSearchModuleController  {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPublicSearchManager publicSearchManager;
    



    @RequestMapping(value = "/exhibit/search/module")
    public ResponseEntity<SearchModuleResults> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<IModule> moduleList = paginationForModule(modulePagenum, searchTerm);                
        SearchModuleResults publicSearchModule  =  publicSearchManager.getSearchModuleResults(moduleList);
        return new ResponseEntity<SearchModuleResults>(publicSearchModule, HttpStatus.OK);
    }



    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the module corresponding to the page number
     * specified in the input parameter(spacePagenum) whose name or description
     * contains the search string. This also filters modules which are linked to the spaces.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    private List<IModule> paginationForModule(String modulePagenum, String searchTerm) {
        Page<IModule> modulePage = publicSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));    
        return  publicSearchManager.updateModuleListWithSpaceInfo(modulePage);
    }

}
