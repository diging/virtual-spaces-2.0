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

import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.StaffSearchSlide;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchSlideController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search/slide")
    public ResponseEntity<StaffSearchSlide> searchInVspace(
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Slide> slideList = paginationForSlide(slidePagenum, searchTerm);
        StaffSearchSlide staffSearch = new StaffSearchSlide();
        staffSearch.setSlides(slideList);

        Map<String, String> slideFirstImage = new HashMap<>();

        for (Slide slide : slideList) {

            String slideFirstImageId = null;

            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImageId = slide.getFirstImageBlock().getImage().getId();
            }
            slideFirstImage.put(slide.getId(), slideFirstImageId);
        }
        staffSearch.setFirstImageOfSlide(slideFirstImage);
        return new ResponseEntity<StaffSearchSlide>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in slide table and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private List<Slide> paginationForSlide(String slidePagenum, String searchTerm) {
        Page<Slide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        return slidePage.getContent();
    }
}
