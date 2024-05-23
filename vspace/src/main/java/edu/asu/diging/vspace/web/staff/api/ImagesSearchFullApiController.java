package edu.asu.diging.vspace.web.staff.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import edu.asu.diging.vspace.core.model.SortByField;
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
            @RequestParam(value = "searchText", required = false) String searchTerm, 
            @RequestParam(value = "imageCat", required = false) String imageCategory,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order,
            Model model, RedirectAttributes attributes) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = Integer.parseInt(page);
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }

        ImageCategory category = null;
        if (imageCategory!=null && imageCategory!="") {
            try {
                category = ImageCategory.valueOf(imageCategory);
            } catch (IllegalArgumentException e) {
                logger.error("Wrong argument for image category", e);
            }
        }
        model.addAttribute("totalPages", category == null ? imageService.getTotalPagesOnSearchText(searchTerm)
                : imageService.getTotalPagesOnSearchText(searchTerm, category));
        model.addAttribute("searchText", searchTerm);
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount(category));
        model.addAttribute("imageCategories", ImageCategory.values());
        model.addAttribute("imageCategory", imageCategory);
        model.addAttribute("searchTerm", searchTerm);
        
        model.addAttribute("sortProperty",
                (sortedBy==null || sortedBy.equals("")) ? SortByField.CREATION_DATE.getValue():sortedBy);
        model.addAttribute("order",
                (order==null || order.equals("")) ? Sort.Direction.DESC.toString().toLowerCase():order);
        
        List<VSImage> imageResults = imageService.getPaginatedImagesBySearchTerm(pageNo, category,
                searchTerm, sortedBy!=null?sortedBy:searchTerm, order!=null ? order : searchTerm);

        Map<String, List<ISpace>> imageToSpaces = new HashMap<>();
        for (IVSImage image : imageResults) {
            List<ISpace> spaces = spaceManager.getSpacesWithImageId(image.getId());
            if (spaces != null && !spaces.isEmpty()) {
                imageToSpaces.put(image.getId(), spaces);
            }
        }
        
        model.addAttribute("images", imageResults);
        model.addAttribute("imageToSpaces", imageToSpaces);
        return "staff/images/imagelist";
    }
}
