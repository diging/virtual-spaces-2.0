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

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/{flag}", method = RequestMethod.DELETE)
    public ResponseEntity<Integer> deleteSlide(@PathVariable("id") String moduleId,
            @PathVariable("slideId") String slideId, @PathVariable("flag") Integer flag) {
        
        try {
            if(flag == 0 && !slideManager.deleteSlideById(slideId, flag)) {
                return new ResponseEntity<Integer>(1, HttpStatus.OK); 
            } else if (flag == 1) {
                slideManager.deleteSlideById(slideId, flag);
            }
        } catch (SlideDoesNotExistException slideDoesNotExistException) {
            logger.error("Could not delete slide.", slideDoesNotExistException);
            return new ResponseEntity<Integer>(2, HttpStatus.NOT_FOUND);
        }       
        return new ResponseEntity<Integer>(0,HttpStatus.OK); 
    }
}
