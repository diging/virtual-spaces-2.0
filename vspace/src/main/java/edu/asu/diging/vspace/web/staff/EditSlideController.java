package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.ISlideManager;

@Controller
public class EditSlideController {

    @Autowired
    private ISlideManager slideManager;

    @Autowired
    private SlideRepository slideRepo;

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/edit/description", method = RequestMethod.POST)
    public ResponseEntity<String> saveDescription(@RequestParam("description") String description,
            @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slide.setDescription(description);
        slideRepo.save((Slide) slide);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{slideId}/edit/title", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitle(@RequestParam("title") String title,
            @PathVariable("moduleId") String moduleId, @PathVariable("slideId") String slideId) {
        ISlide slide = slideManager.getSlide(slideId);
        slide.setName(title);
        slideRepo.save((Slide) slide);
        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
