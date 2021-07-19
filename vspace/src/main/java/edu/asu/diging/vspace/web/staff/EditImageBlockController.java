package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class EditImageBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @Autowired
    private IImageService imageService;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/image/{imageBlockId}", method = RequestMethod.POST)
    public ResponseEntity<String> editImageBlock(@PathVariable("id") String slideId,
            @PathVariable("imageBlockId") String blockId, @PathVariable("moduleId") String moduleId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("contentOrder") Integer contentOrder, Principal principal,
            @RequestParam(value = "imageId", required = false) String imageId, RedirectAttributes attributes)
            throws IOException {

        IImageBlock imageBlock = contentBlockManager.getImageBlock(blockId);
        if (imageId != null && !imageId.trim().isEmpty()) {
            IVSImage image = null;
            try {
                image = imageService.getImageById(imageId);
            } catch (ImageDoesNotExistException e) {
                logger.error("Image does not exist.", e);
                return new ResponseEntity<>(imageId, HttpStatus.BAD_REQUEST);
            }
            contentBlockManager.updateImageBlock(imageBlock, image);
        } else {
            byte[] image = null;
            String filename = null;
            if (file != null) {
                image = file.getBytes();
                filename = file.getOriginalFilename();
                try {
                    contentBlockManager.updateImageBlock(imageBlock, image, filename, contentOrder);
                } catch (ImageCouldNotBeStoredException e) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode node = mapper.createObjectNode();
                    node.put("errorMessage", "Image Content block cannot be stored.");
                    return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}