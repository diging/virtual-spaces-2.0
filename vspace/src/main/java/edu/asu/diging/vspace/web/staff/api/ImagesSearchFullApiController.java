package edu.asu.diging.vspace.web.staff.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ImagesSearchFullApiController {

    @Autowired
    private IImageService imageService;

    @Autowired
    private ISpaceManager spaceManager;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/staff/images/search/full/{page}")
    public String imageSearchDescription(@PathVariable String page,
            @RequestParam(value = "searchText", required = false) String searchTerm, Model model,
            RedirectAttributes attributes) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = Integer.parseInt(page);
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }
        ImageCategory category = null;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                category = ImageCategory.valueOf(searchTerm);
            } catch (IllegalArgumentException e) {
                logger.error("Wrong argument for image category", e);
            }
        }
        model.addAttribute("totalPages", category == null ? imageService.getTotalPagesOnSearchText(searchTerm)
                : imageService.getTotalPages(category));
        model.addAttribute("searchText", searchTerm);
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount(category));

        List<VSImage> imageResults = imageService.getPaginatedImagesByCategoryAndSearchTerm(pageNo, category,
                searchTerm);

        Map<String, List<ISpace>> imageToSpaces = new HashMap<>();
        for (IVSImage image : imageResults) {
            List<ISpace> spaces = spaceManager.getSpacesWithImageId(image.getId());
            if (spaces != null && !spaces.isEmpty()) {
                imageToSpaces.put(image.getId(), spaces);
            }
        }

        model.addAttribute("imageToSpaces", imageToSpaces);
        model.addAttribute("images", imageResults);
        model.addAttribute("searchTerm", searchTerm);
        return "staff/images/imagelist";
    }
}
