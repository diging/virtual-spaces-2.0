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
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.services.IContentBlockManager;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.CreationReturnValue;

@Controller
public class AddImageBlockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IContentBlockManager contentBlockManager;

    @Autowired
    private IImageService imageService;
    
    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}/image", method = RequestMethod.POST)
    public ResponseEntity<String> addImageBlock(@PathVariable("id") String slideId,
            @PathVariable("moduleId") String moduleId,
            @RequestParam(value = "file", required = false) MultipartFile file, Principal principal,
            @RequestParam(value = "imageId", required = false) String imageId, RedirectAttributes attributes)
            throws IOException {

        Integer contentOrder = contentBlockManager.findMaxContentOrder(slideId);
        contentOrder = contentOrder == null ? 0 : contentOrder + 1;
        
        IVSpaceElement imageBlock;
        String imageBlockId = null;
        if (imageId != null && !imageId.trim().isEmpty()) {
            IVSImage image;
            try {
                image = imageService.getImageById(imageId);
            } catch (ImageDoesNotExistException e) {
                logger.error("Image does not exist.", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            CreationReturnValue imageBlockReturnValue = contentBlockManager.createImageBlock(slideId, image,
                    contentOrder);
            imageBlock = imageBlockReturnValue.getElement();
            imageBlockId = imageBlock.getId();
        } else {
            if (file != null) {
                byte[] image = file.getBytes();
                String filename = file.getOriginalFilename();
                try {
                    CreationReturnValue imageBlockReturnValue = contentBlockManager.createImageBlock(slideId, image,
                            filename, contentOrder);
                    imageBlock = imageBlockReturnValue.getElement();
                } catch (ImageCouldNotBeStoredException e) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode node = mapper.createObjectNode();
                    node.put("errorMessage", "Image Content block cannot be stored.");
                    return new ResponseEntity<>(mapper.writeValueAsString(node), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                imageBlockId = imageBlock.getId();
            }
        }
        return new ResponseEntity<>(imageBlockId, HttpStatus.OK);
    }
}