package edu.asu.diging.vspace.web.staff;

import java.util.List;

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
import edu.asu.diging.vspace.core.model.impl.Sequence;
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
        } catch (Exception exception) {
            logger.error("Could not delete slide.", exception);
            return new ResponseEntity<String>("Sorry, unable to delete the slide. The slide does not exist.",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}/sequences", method = RequestMethod.GET)
    public ResponseEntity<List<Sequence>> checkSlideInSequence(@PathVariable("id") String moduleId,
            @PathVariable("slideId") String slideId) {

        List<Sequence> slideSequences = slideManager.getSlideSequences(slideId, moduleId);
        return new ResponseEntity<>(slideSequences, HttpStatus.OK);
    }
}
