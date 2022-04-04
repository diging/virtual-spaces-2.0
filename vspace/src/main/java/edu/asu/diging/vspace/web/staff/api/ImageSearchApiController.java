package edu.asu.diging.vspace.web.staff.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ImageSearchApiController {
    
    @Autowired
    private IImageService imageService;
    
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping("/staff/images/search")
    public ResponseEntity<String> searchImage(@RequestParam(value = "term", required = false) String searchTerm) {
        List<IVSImage> images = null;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            images = imageService.findByFilenameOrNameContains(searchTerm);
        } else {
            images = imageService.getImages(1);
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode idArray = mapper.createArrayNode();
        for (IVSImage image : images) {
            ObjectNode imageNode = mapper.createObjectNode();
            imageNode.put("id", image.getId());
            if (image.getName() != null && !image.getName().isEmpty()) {
                imageNode.put("text", image.getName() + " (" + image.getFilename() + ")");
            } else {
                 imageNode.put("text", image.getFilename());
            }
            idArray.add(imageNode);
        }
        return new ResponseEntity<String>(idArray.toString(), HttpStatus.OK);
    }
    
    @RequestMapping("/staff/images/searchImagesncd")
    public String searchImageDescription(@RequestParam(value = "searchText", required = false) String searchTerm , Model model, RedirectAttributes attributes) {
        List<IVSImage> images = null;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
        	
            images = imageService.findByFilenameOrNameContainsOrDescription(searchTerm);
        } else {
            images = imageService.getImages(1);
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
        return "staff/images/imagelist";
         
        
    }
    
    
}
