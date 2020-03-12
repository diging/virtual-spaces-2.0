package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class ListImagesController {

    @Autowired
    private IImageService imageService;

    @RequestMapping("/staff/images/list/{page}")
    public String listImagesByPage(@PathVariable String page, @RequestParam(value = "sort", required = false) String sortedBy, @RequestParam(value = "order", required = false) String order, Model model) {
        int pageNo;
        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }
        model.addAttribute("totalPages", imageService.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        model.addAttribute("images", imageService.getImages(pageNo, sortedBy, order));
        model.addAttribute("imageCategories", ImageCategory.values());
        model.addAttribute("sortProperty", sortedBy);
        model.addAttribute("order", order);
        
        return "staff/images/list";
    }
    
}
