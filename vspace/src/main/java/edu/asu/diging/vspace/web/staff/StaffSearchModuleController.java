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
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchModuleResults;

@Controller
public class StaffSearchModuleController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/staff/search/module")
    public ResponseEntity<StaffSearchModuleResults> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Module> moduleList = paginationForModule(modulePagenum, searchTerm);
        StaffSearchModuleResults staffSearch = new StaffSearchModuleResults();
        staffSearch.setModules(moduleList);

        Map<String, String> moduleFirstSlideImage = new HashMap<>();
        Map<String, Boolean> isModuleConfiguredMap = new HashMap<>();

        for (Module module : moduleList) {
            if (module.getStartSequence() == null) {
                isModuleConfiguredMap.put(module.getId(), false);
                moduleFirstSlideImage.put(module.getId(), null);
            } else {
                isModuleConfiguredMap.put(module.getId(), true);
                String startSequenceID = module.getStartSequence().getId();
                List<ISlide> slides = sequenceManager.getSequence(startSequenceID) != null
                        ? sequenceManager.getSequence(startSequenceID).getSlides()
                        : null;

                Slide slide = slides != null && !slides.isEmpty() ? (Slide) slides.get(0) : null;

                if (slide != null && slide.getFirstImageBlock() != null) {
                    moduleFirstSlideImage.put(module.getId(), slide.getFirstImageBlock().getImage().getId());
                }
            }
        }
        staffSearch.setModuleImageIdMap(moduleFirstSlideImage);
        staffSearch.setModuleAlertMessages(isModuleConfiguredMap);
        return new ResponseEntity<StaffSearchModuleResults>(staffSearch, HttpStatus.OK);
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
    private List<Module> paginationForModule(String modulePagenum, String searchTerm) {
        Page<Module> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        return modulePage.getContent();
    }

}
