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
import edu.asu.diging.vspace.core.model.impl.StaffSearchModule;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchModuleController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/staff/search/module")
    public ResponseEntity<StaffSearchModule> searchInVspace(
            @RequestParam(value = "modulePagenum", required = false, defaultValue = "1") String modulePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Module> moduleList = paginationForModule(modulePagenum, searchTerm);
        StaffSearchModule staffSearch = new StaffSearchModule();
        staffSearch.setModules(moduleList);

        Map<String, String> moduleFirstSlideImage = new HashMap<>();
        Map<String, Boolean> moduleAlertMessage = new HashMap<>();

        for (Module module : moduleList) {

            if (module.getStartSequence() == null) {
                moduleAlertMessage.put(module.getId(), true);
                moduleFirstSlideImage.put(module.getId(), null);
            } else {
                moduleAlertMessage.put(module.getId(), false);
                String startSequenceID = module.getStartSequence().getId();
                List<ISlide> slides = sequenceManager.getSequence(startSequenceID) != null
                        ? sequenceManager.getSequence(startSequenceID).getSlides()
                        : null;

                Slide slide = slides != null && !slides.isEmpty() ? (Slide) slides.get(0) : null;
                String firstSlideImageId = null;

                if (slide != null && slide.getFirstImageBlock() != null) {
                    firstSlideImageId = slide.getFirstImageBlock().getImage().getId();
                }
                moduleFirstSlideImage.put(module.getId(), firstSlideImageId);
            }
        }
        staffSearch.setFirstImageOfFirstSlideForModule(moduleFirstSlideImage);
        staffSearch.setModuleAlertMessages(moduleAlertMessage);
        return new ResponseEntity<StaffSearchModule>(staffSearch, HttpStatus.OK);
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
    private List<Module> paginationForModule(String modulePagenum, String searchTerm) {
        Page<Module> modulePage = staffSearchManager.searchInModules(searchTerm, Integer.parseInt(modulePagenum));
        return modulePage.getContent();
    }

}
