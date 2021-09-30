package edu.asu.diging.vspace.web.general.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

@Controller
public class PublicSearchSlideTextController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private IStaffSearchManager staffSearchManager;
    
    @Autowired
    private ISlideManager slideManager;

    @RequestMapping(value = "/exhibit/search/slideText")
    public ResponseEntity<PublicSearchSlideTextBlockResults> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<ISlide> slideTextList = paginationForSlideText(slideTextPagenum, searchTerm);
        PublicSearchSlideTextBlockResults publicSearch = new PublicSearchSlideTextBlockResults();
        publicSearch.setSlidesWithMatchedTextBlock(slideTextList);
        
        Map<String, String> slideTextFirstImageMap = new HashMap<>();
        Map<String, String> slideTextFirstTextBlockMap = new HashMap<>();
        
        for (ISlide slide : slideTextList) {
            if (slide != null) {
                if (slide.getFirstImageBlock() != null) {
                    slideTextFirstImageMap.put(slide.getId(), slide.getFirstImageBlock().getImage().getId());
                }
                if (slide.getFirstMatchedTextBlock(searchTerm) != null) {
                    slideTextFirstTextBlockMap.put(slide.getId(), slide.getFirstMatchedTextBlock(searchTerm).htmlRenderedText());
                }
            }
        }
        publicSearch.setSlideToFirstImageMap(slideTextFirstImageMap);
        publicSearch.setSlideToFirstTextBlockMap(slideTextFirstTextBlockMap);
        return new ResponseEntity<PublicSearchSlideTextBlockResults>(publicSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) and return the slides corresponding to the page number
     * specified in the input parameter(spacePagenum) whose text block contains the
     * search string. This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private List<ISlide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<ISlide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        List<ISlide> slideTextList = new ArrayList<>();
        Set<String> slideSet = slideManager.getAllSlidesFromStartSequences();
        
        for(ISlide slide : slideTextPage.getContent()) {
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
                    slideTextList.add(slideWithSpace);
                }
            }
        }
        return slideTextList;
    }
}
