package edu.asu.diging.vspace.web.staff;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Controller
public class SequenceController {

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{id}/slides", method = RequestMethod.GET)
    public ResponseEntity<List<ISlide>> getSequenceSlides(Model model, @PathVariable("moduleId") String moduleId,
            @PathVariable("id") String sequenceId, @ModelAttribute SequenceForm sequenceForm, Principal principal) {

        List<ISlide> slides = sequenceManager.getSequence(sequenceId).getSlides();
        return new ResponseEntity<List<ISlide>>(slides, HttpStatus.OK);
    }

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/{id}", method = RequestMethod.GET)
    public String getSequence(Model model, @PathVariable("moduleId") String moduleId,
            @PathVariable("id") String sequenceId, @ModelAttribute SequenceForm sequenceForm, Principal principal) {

        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("sequence", sequenceManager.getSequence(sequenceId));
        model.addAttribute("selectedSlides", sequenceManager.getSequence(sequenceId).getSlides());
        model.addAttribute("allSlides", moduleManager.getModuleSlides(moduleId));
        return "staff/modules/sequences/sequence";
    }
}