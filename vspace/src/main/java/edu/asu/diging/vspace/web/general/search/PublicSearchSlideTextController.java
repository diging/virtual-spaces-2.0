package edu.asu.diging.vspace.web.general.search;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.SlideWithSpace;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class PublicSearchSlideTextController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/exhibit/search/slideText")
    public ResponseEntity<PublicSearchSlideText> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Slide> slideTextList = paginationForSlideText(slideTextPagenum, searchTerm);
        PublicSearchSlideText publicSearchSlideText = new PublicSearchSlideText();
        publicSearchSlideText.setSlideTextList(slideTextList);
        
        Map<String, String> slideTextFirstImage = new HashMap<>();
        
        for (Slide slide : slideTextList) {
            
            String slideFirstImageId = null;
            
            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImageId = slide.getFirstImageBlock().getImage().getId();
            }
            slideTextFirstImage.put(slide.getId(), slideFirstImageId);
            publicSearchSlideText.setSlideTextFirstImage(slideTextFirstImage);
        }
        return new ResponseEntity<PublicSearchSlideText>(publicSearchSlideText, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in ContentBlock table and return the slides
     * corresponding to the page number specified in the input
     * parameter(spacePagenum) whose text block contains the search string.
     * This also filters Slides from modules which are linked to the spaces.
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private List<Slide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<Slide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        List<Slide> slideTextList = new ArrayList<>();
        
        for(Slide slide : slideTextPage.getContent()) {
            ModuleLink moduleLink = moduleLinkManager.findFirstByModule(slide.getModule());
            if(moduleLink!=null) {
                SlideWithSpace slideWithSpace = new SlideWithSpace();
                try {
                    BeanUtils.copyProperties(slideWithSpace, slide);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Could not create moduleWithSpace.", e);
                }
                slideWithSpace.setSpaceId(moduleLink.getSpace().getId());
                slideTextList.add(slideWithSpace);
            }
        }
        return slideTextList;
    }
}
