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
        int currentPage = Integer.parseInt(page);
        long totalPages = imageService.getTotalPages();
        currentPage = (currentPage<1 || currentPage>totalPages)?1:currentPage;
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        model.addAttribute("images", imageService.getRequestedImages(currentPage));
        return "staff/images/list";
    }
}
