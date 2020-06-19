package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Controller
public class EditSequenceController {

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private ISlideManager slideManager;

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{sequenceId}/edit/description", method = RequestMethod.POST)
    public ResponseEntity<String> saveDescription(@RequestParam("description") String description,
            @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        ISequence sequence = sequenceManager.getSequence(sequenceId);
        sequence.setDescription(description);
        sequenceManager.updateSequence(sequence);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{sequenceId}/edit/title", method = RequestMethod.POST)
    public ResponseEntity<String> saveTitle(@RequestParam("title") String title,
            @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        ISequence sequence = sequenceManager.getSequence(sequenceId);
        sequence.setName(title);
        sequenceManager.updateSequence(sequence);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{sequenceId}/edit/slides", method = RequestMethod.POST)
    public String saveOrderedSlides(@ModelAttribute SequenceForm sequenceForm,
            @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId) {
        ISequence sequence = sequenceManager.getSequence(sequenceId);
        List<ISlide> slides = new ArrayList<>();
        for (String slideId : sequenceForm.getOrderedSlides()) {
            slides.add(slideManager.getSlide(slideId));
        }
        sequence.setSlides(slides);
        sequenceManager.updateSequence(sequence);
        return "redirect:/staff/module/{moduleId}";
    }
}