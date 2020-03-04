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
            slideManager.deleteSlideById(slideId, moduleId);
        } catch (SlideDoesNotExistException slideDoesNotExistException) {
            logger.error("Could not delete slide.", slideDoesNotExistException);
            return new ResponseEntity<String>("Sorry, unable to delete the slide. The slide does not exist.",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/sequences", method = RequestMethod.POST)
    public ResponseEntity<String> checkDeleteSlide(@PathVariable("id") String moduleId,
            @PathVariable("slideId") String slideId) {

        boolean checkSlideHasSequence = slideManager.checkSlideHasSequence(slideId, moduleId);
        if(checkSlideHasSequence)
            return new ResponseEntity<String>("1", HttpStatus.OK);
        else
            return new ResponseEntity<String>("0",
                    HttpStatus.OK);

    }
}
