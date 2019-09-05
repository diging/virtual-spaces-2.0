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

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Controller
public class ModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping("/staff/module/{id}")
    public String showModule(@PathVariable String id, Model model) {

        model.addAttribute("module", moduleManager.getModule(id));
        model.addAttribute("slides", moduleManager.getModuleSlides(id));
        model.addAttribute("sequences", moduleManager.getModuleSequences(id));
        return "staff/module";
    }
    
    @RequestMapping(value = "/staff/module/{moduleId}/sequences", method = RequestMethod.GET)
    public ResponseEntity<List<ISequence>> getModuleSequences(Model model, @PathVariable("moduleId") String moduleId, @ModelAttribute SequenceForm sequenceForm,
            Principal principal) {

        List<ISequence> sequences = moduleManager.getModuleSequences(moduleId);
        return new ResponseEntity<List<ISequence>>(sequences, HttpStatus.OK);
    }
}
