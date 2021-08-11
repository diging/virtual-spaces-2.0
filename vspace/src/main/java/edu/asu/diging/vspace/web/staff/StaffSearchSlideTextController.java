package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.StaffSearchSlideTextBlock;
import edu.asu.diging.vspace.core.services.IStaffSearchManager;

@Controller
public class StaffSearchSlideTextController {

    @Autowired
    private IStaffSearchManager staffSearchManager;

    @RequestMapping(value = "/staff/search/slideText")
    public ResponseEntity<StaffSearchSlideTextBlock> searchInVspace(HttpServletRequest request,
            @RequestParam(value = "slideTextPagenum", required = false, defaultValue = "1") String slideTextPagenum,
            Model model, @RequestParam(name = "searchText") String searchTerm) {

        List<Slide> slideTextList = paginationForSlideText(slideTextPagenum, searchTerm);
        StaffSearchSlideTextBlock staffSearch = new StaffSearchSlideTextBlock();
        staffSearch.setSlidesWithMatchedTextBlock(slideTextList);

        Map<String, String> slideTextFirstImageMap = new HashMap<>();

        Map<String, String> slideTextFirstTextBlockMap = new HashMap<>();

        for (Slide slide : slideTextList) {

            String slideFirstImageId = null;

            if (slide != null && slide.getFirstImageBlock() != null) {
                slideFirstImageId = slide.getFirstImageBlock().getImage().getId();
            }
            slideTextFirstImageMap.put(slide.getId(), slideFirstImageId);

            ITextBlock slideFirstTextBlock = null;

            if (slide != null && slide.getFirstMatchedTextBlock(searchTerm)!= null) {
                slideFirstTextBlock = slide.getFirstMatchedTextBlock(searchTerm);
            }
            slideTextFirstTextBlockMap.put(slide.getId(), slideFirstTextBlock.getText());

        }
        staffSearch.setSlideTextFirstImage(slideTextFirstImageMap);
        staffSearch.setSlideTextFirstTextBlock(slideTextFirstTextBlockMap);
        return new ResponseEntity<StaffSearchSlideTextBlock>(staffSearch, HttpStatus.OK);
    }

    /**
     * This method is used to search the searched string specified in the input
     * parameter(searchTerm) in ContentBlock table and return the slides
     * corresponding to the page number specified in the input
     * parameter(spacePagenum) whose text block contains the search string
     * 
     * @param slideTextPagenum current page number sent as request parameter in the
     *                         URL.
     * @param searchTerm       This is the search string which is being searched.
     */
    private List<Slide> paginationForSlideText(String slideTextPagenum, String searchTerm) {
        Page<Slide> slideTextPage = staffSearchManager.searchInSlideTexts(searchTerm,
                Integer.parseInt(slideTextPagenum));
        return slideTextPage.getContent();
    }
}
