
package edu.asu.diging.vspace.web.staff;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
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
    
//    @RequestMapping(value = "/staff/module/{moduleId}/sequence/get/{id}", method = RequestMethod.GET)
//    public ResponseEntity<Sequence> getSequenceSlides(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("id") String sequenceId, 
//            Principal principal) {
//        
//        Sequence seq = (Sequence) sequenceManager.getSequence(sequenceId);//.getSlides();
////        Map<String, String> data = new HashMap<String, String>();
////        for(ISlide x: slides) {
////            data.put(x.getName(), x.getDescription());
////        }
//
//        //Map<String, Slide> data = new HashMap<String, Slide>();
//
//        return new ResponseEntity<Sequence>(seq, HttpStatus.OK);
//    }
}
