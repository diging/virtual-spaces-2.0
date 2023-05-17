package edu.asu.diging.vspace.web.publicpages;

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

        return new ResponseEntity<SearchModuleResults>(publicSearchManager.searchForModule(modulePagenum, searchTerm), HttpStatus.OK);
    }

}
