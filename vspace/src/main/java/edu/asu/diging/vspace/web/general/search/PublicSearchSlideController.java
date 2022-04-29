package edu.asu.diging.vspace.web.general.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
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

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.SlideWithSpace;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;
import edu.asu.diging.vspace.core.services.impl.model.StaffSearchSlideResults;

@Controller
public class PublicSearchSlideController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;
    
    @Autowired
    private ISlideManager slideManager;
    
    @RequestMapping(value = "/exhibit/search/slide")
    public ResponseEntity<StaffSearchSlideResults> searchInVspace(
            @RequestParam(value = "slidePagenum", required = false, defaultValue = "1") String slidePagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideList = paginationForSlide(slidePagenum, searchTerm);
        StaffSearchSlideResults publicSearch = new StaffSearchSlideResults();
        publicSearch.setSlides(slideList);
        
        Map<String, String> slideFirstImage = new HashMap<>();
        
        for (ISlide slide : slideList) {
            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImage.put(slide.getId(), slide.getFirstImageBlock().getImage().getId());
            }
        }
        publicSearch.setFirstImageOfSlide(slideFirstImage);
        return new ResponseEntity<StaffSearchSlideResults>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the search string specified in the input
     * parameter(searchTerm) and return the slides corresponding to
     * the page number specified in the input parameter(spacePagenum) whose name or
     * description contains the search string. This also filters Slides from modules 
     * which are linked to the spaces.
     * 
     * @param slidePagenum current page number sent as request parameter in the URL.
     * @param searchTerm   This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlide(String slidePagenum, String searchTerm) {
        Page<ISlide> slidePage = staffSearchManager.searchInSlides(searchTerm, Integer.parseInt(slidePagenum));
        List<ISlide> slideList = new ArrayList<>();
        Set<String> slideSet = slideManager.getAllSlidesFromStartSequences();
        
        for(ISlide slide : slidePage.getContent()) {
            if(slideSet.contains(slide.getId())) {
                ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
                if(moduleLink!=null) {
                    SlideWithSpace slideWithSpace = new SlideWithSpace();
                    try {
                        BeanUtils.copyProperties(slideWithSpace, slide);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        logger.error("Could not create moduleWithSpace.", e);
                    }
                    slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                    slideWithSpace.setStartSequenceId(slide.getModule().getStartSequence().getId());
                    slideList.add(slideWithSpace);
                }
            }
        }
        return slideList;
    }
}
