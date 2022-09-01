package edu.asu.diging.vspace.web.staff.api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ImagesSearchFullApiController {

    @Autowired
	private IImageService imageService;

    @Autowired
	private ISpaceManager spaceManager;

    @RequestMapping("/staff/images/search/full")
	public String imageSearchFull(Model model,
			@RequestParam(value = "searchText", required = false) String searchTerm) {
        return String.format("redirect:/staff/images/search/full/1?searchText=%s");
    }

    @RequestMapping("/staff/images/search/full/{page}")
	public String imageSeacrhDescription(@PathVariable(required = false) String page,
			@RequestParam(value = "searchText", required = false) String searchTerm, Model model,
			RedirectAttributes attributes) {

        List<IVSImage> images = null;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {

            images = imageService.search(searchTerm);
        } else {
            images = imageService.getImages(1);
        }
        int pageNo;
        page = StringUtils.isEmpty(page) ? "1" : page;

        try {
            pageNo = imageService.validatePageNumber(Integer.parseInt(page), images);
        } catch (NumberFormatException numberFormatException) {
            pageNo = 1;
        }

        model.addAttribute("totalPages", imageService.getTotalPages(images));
        model.addAttribute("currentPageNumber", pageNo);
        model.addAttribute("totalImageCount", images.size());
        List<IVSImage> imageResults = imageService.getImagesForPagination(pageNo, images, searchTerm);
        Map<String, List<ISpace>> imageToSpaces = new HashMap<String, List<ISpace>>();
        for (IVSImage image : imageResults) {
            List<ISpace> spaces = spaceManager.getSpacesWithImageId(image.getId());
            if (spaces != null && spaces.size() > 0) {
                imageToSpaces.put(image.getId(), spaces);
            }
		}
        model.addAttribute("imageToSpaces", imageToSpaces);
        model.addAttribute("images", imageResults);
        model.addAttribute("searchTerm", searchTerm);
        return "staff/images/searchImages";
    }
}
