package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class ListImagesController {

    @Autowired
    private IImageService imageService;

    @RequestMapping({"/staff/images/list/{page}","/staff/images/list"})
    public String listSpaces(@PathVariable(required = false) String page, Model model) {
        System.out.println("--"+page);
        int pageNo;
        if(page == null || page.isEmpty()) {
            page = "1";
        }
        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }
        model.addAttribute("totalPages", imageService.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        model.addAttribute("images", imageService.getImages(pageNo));
        model.addAttribute("imageCategories", ImageCategory.values());
        
        return "staff/images/list";
    }
    
}
