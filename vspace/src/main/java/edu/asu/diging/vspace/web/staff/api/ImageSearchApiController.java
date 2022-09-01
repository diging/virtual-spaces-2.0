package edu.asu.diging.vspace.web.staff.api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class ImageSearchApiController {

    @Autowired
	private IImageService imageService;

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
    }
