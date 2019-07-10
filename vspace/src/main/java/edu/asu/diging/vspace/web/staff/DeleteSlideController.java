package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class DeleteSlideController {

    @Autowired
    private ISlideManager slideManager;

    @RequestMapping(value = "/staff/module/{id}/slide/{slideId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteModuleLink(@PathVariable("id") String spaceId,
            @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slideManager.deleteSlide(slide);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
