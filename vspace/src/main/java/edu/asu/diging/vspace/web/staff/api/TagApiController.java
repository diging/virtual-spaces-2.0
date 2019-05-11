package edu.asu.diging.vspace.web.staff.api;

import java.security.Principal;
import java.util.Optional;

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

import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;

@Controller
public class TagApiController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IImageService imageService;

    @RequestMapping(value = "/staff/images/{imageId}/tag", method = RequestMethod.POST)
    public ResponseEntity<String> updateTag(Principal principal, @PathVariable("imageId") String imageId, @RequestParam("tag") String tag) {
        
        IVSImage image;
        try {
            image = imageService.getImageById(imageId);
        } catch (ImageDoesNotExistException e) {
            logger.error("Image not found.", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!image.getCreatedBy().equals(principal.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        ImageCategory category;
        try {
            category = ImageCategory.valueOf(tag);
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid image category.", ex);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        imageService.addCategory(image, category);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
