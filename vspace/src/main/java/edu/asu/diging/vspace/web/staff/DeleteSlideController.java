package edu.asu.diging.vspace.web.staff;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class DeleteSlideController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ISlideManager slideManager;

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteSlide(@PathVariable("id") String moduleId,
            @PathVariable("slideId") String slideId) {
        
        try {
            if(!slideManager.deleteSlideById(slideId)) {
                return new ResponseEntity<String>("slide exists", HttpStatus.BAD_REQUEST); 
            }
            
        } catch (SlideDoesNotExistException slideDoesNotExistException) {
            logger.error("Could not delete slide.", slideDoesNotExistException);
            return new ResponseEntity<String>("Invalid input. Please try again", HttpStatus.NOT_FOUND);
        }       
        return new ResponseEntity<String>(HttpStatus.OK); 
    }
}
