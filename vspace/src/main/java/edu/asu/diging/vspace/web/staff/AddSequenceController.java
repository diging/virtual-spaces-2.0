
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
    
    @RequestMapping(value = "/staff/module/{moduleId}/sequence/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ISlide>> getSequenceSlides(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("id") String sequenceId, @ModelAttribute SequenceForm sequenceForm,
            Principal principal) {
//        List<String> str = new ArrayList<>();
//        str.add("hi");       
        List<ISlide> slide = sequenceManager.getSequence(sequenceId).getSlides();
        return new ResponseEntity<List<ISlide>>(slide, HttpStatus.OK);
    }
}
