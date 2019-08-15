
package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.forms.SequenceForm;

@Controller
public class AddSequenceController {

    @Autowired
    private ISequenceManager sequenceManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    @RequestMapping(value = "/staff/module/{id}/sequence/add", method = RequestMethod.GET)
    public String showAddSequence(@PathVariable("id") String moduleId, Model model) {
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("sequence", new SequenceForm());
        model.addAttribute("slides", moduleManager.getModuleSlides(moduleId));
        return "staff/module/sequence/add";
    }
    
    @RequestMapping(value = "/staff/module/{moduleId}/sequence/add", method = RequestMethod.POST)
    public String addSequence(Model model, @PathVariable("moduleId") String moduleId, @ModelAttribute SequenceForm sequenceForm,
            Principal principal) {

        sequenceManager.storeSequence(moduleId, sequenceForm); 

        return "redirect:/staff/module/{moduleId}";
    }
}
