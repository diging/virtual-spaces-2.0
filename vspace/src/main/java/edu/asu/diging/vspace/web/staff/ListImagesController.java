package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class ListImagesController {

    @Autowired
    private IImageService imageService;

    @RequestMapping(value={"/staff/images/list/", "/staff/images/list/{page}"})
    public String listSpaces(@PathVariable(required=false) String page, Model model) {
        if (page == null || page.trim().isEmpty()) {
            page = "1";
        }
        int pageNo;
        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }
        model.addAttribute("totalPages", imageService.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        model.addAttribute("images", imageService.getImages(pageNo));
        return "staff/images/list";
    }
}
