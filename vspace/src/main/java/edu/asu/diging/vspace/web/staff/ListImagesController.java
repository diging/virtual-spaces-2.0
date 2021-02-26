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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String listSpacesWithoutNum(Model model, @RequestParam(value = "imageCat", required = false) String imageCategory,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order) {
        return String.format("redirect:/staff/images/list/1?imageCat=%s&sort=%s&order=%s",(imageCategory==null?"":imageCategory),sortedBy,order);
    }

    @RequestMapping("/staff/images/list/{page}")
    public String listSpaces(@PathVariable(required = false) String page,
            @RequestParam(value = "imageCat", required = false) String imageCategory,
            @RequestParam(value = "sort", required = false) String sortedBy,
            @RequestParam(value = "order", required = false) String order, Model model, RedirectAttributes attributes) {
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;
        ImageCategory category = null;
        if(!imageCategory.isEmpty()) {
            try {
                category = ImageCategory.valueOf(imageCategory);
            }catch(IllegalArgumentException e) {
                category=null;
            }
            
        }
        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page),category);
        } catch (NumberFormatException numberFormatException){
            pageNo = 1;
        }
        List<IVSImage> images;
        model.addAttribute("totalPages", imageService.getTotalPages(category));
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", imageService.getTotalImageCount(category));
        images = imageService.getImages(pageNo, category, sortedBy, order);
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
