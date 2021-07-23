package edu.asu.diging.vspace.web.general.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.StaffSearch;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class PublicSearchModuleController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/exhibit/search/module")
    public ResponseEntity<StaffSearch> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        HashSet<Module> moduleSet = paginationForModule(modulePagenum, searchTerm);
        StaffSearch staffSearch = new StaffSearch();
        staffSearch.setModuleSet(moduleSet);
        
        Map<String, String> moduleFirstSlideImage = new HashMap<>();
        
        for (Module module : moduleSet) {
            
            Slide slide = module.getSlides() != null && !module.getSlides().isEmpty()
                    ? (Slide) module.getSlides().get(0) : null;
            
            String firstSlideImageId = null;
            
            if (slide != null && slide.getFirstImageBlock() != null) {
                firstSlideImageId = slide.getFirstImageBlock().getImage().getId();
            }
            moduleFirstSlideImage.put(module.getId(), firstSlideImageId);
            staffSearch.setModuleFirstSlideFirstImage(moduleFirstSlideImage);
        }
        return new ResponseEntity<StaffSearch>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in module table and return the module corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param modulePagenum current page number sent as request parameter in the
     *                      URL.
     * @param searchTerm    This is the search string which is being searched.
     */
    private HashSet<Module> paginationForModule(String modulePagenum, String searchTerm) {
        Page<Module> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        HashSet<Module> moduleSet = new LinkedHashSet<>();
        moduleSet.addAll(modulePage.getContent());
        return moduleSet;
    }

}
