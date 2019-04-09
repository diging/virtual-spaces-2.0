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

    @RequestMapping("/staff/images/list/{page}")
    public String listSpaces(@PathVariable String page, Model model) {
        long totalPages = imageService.getTotalPages();
        int currentPage;
        try {
            currentPage = Integer.parseInt(page);
            currentPage = (currentPage<1 || currentPage>totalPages)?1:currentPage;
        } catch (NumberFormatException numberFormatException) {
            currentPage = 1;
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPageNumber", currentPage);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        model.addAttribute("images", imageService.getRequestedImages(currentPage, totalPages));
        return "staff/images/list";
    }
}
