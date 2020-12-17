package edu.asu.diging.vspace.web.staff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ListImagesController {

    @Autowired
    private IImageService imageService;

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/images/list")
    public String listSpacesWithoutNum(Model model, @RequestParam(value = "imageCat", required = false) String imageCategory) {

        return String.format("redirect:/staff/images/list/1?imageCat=%s",(imageCategory != null ? imageCategory : Constants.ALL));
    }

    @RequestMapping("/staff/images/list/{page}")
    public String listSpaces(@PathVariable(required = false) String page,
            @RequestParam(value = "imageCat", required = false) String imageCategory,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order, Model model) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page));
        } catch (NumberFormatException numberFormatException){
            pageNo = 1;
        }
        model.addAttribute("totalPages", imageService.getTotalPages());
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount());
        List<IVSImage> images;
        if(imageCategory.equals(Constants.ALL)) {
            model.addAttribute("totalPages", imageService.getTotalPages());
            model.addAttribute("currentPageNumber", pageNo);
            model.addAttribute("totalImageCount", imageService.getTotalImageCount());
            images = imageService.getImages(pageNo, sortedBy, order);
        } else {
            model.addAttribute("totalPages", imageService.getTotalPages(ImageCategory.valueOf(imageCategory)));
            model.addAttribute("currentPageNumber", pageNo);
            model.addAttribute("totalImageCount", imageService.getTotalImageCount(ImageCategory.valueOf(imageCategory)));
            images = imageService.getImages(pageNo, ImageCategory.valueOf(imageCategory), sortedBy, order);
        }
        Map<String, List<ISpace>> imageToSpaces = new HashMap<String, List<ISpace>>();
        for(IVSImage image : images) {
            List<ISpace> spaces = spaceManager.getSpacesWithImageId(image.getId());
            if(spaces!=null && spaces.size()>0) {
                imageToSpaces.put(image.getId(), spaces);
            }
        }
        model.addAttribute("imageToSpaces",imageToSpaces);
        model.addAttribute("images", images);
        model.addAttribute("imageCategories", ImageCategory.values());
        model.addAttribute("imageCategory", imageCategory);
        model.addAttribute("sortProperty",
                (sortedBy==null || sortedBy.equals("")) ? SortByField.CREATION_DATE.getValue():sortedBy);
        model.addAttribute("order",
                (order==null || order.equals("")) ? Sort.Direction.DESC.toString().toLowerCase():order);
        return "staff/images/imagelist";
    }
}
