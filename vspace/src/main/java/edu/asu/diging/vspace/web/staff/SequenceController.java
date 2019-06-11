package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Controller
public class SequenceController {

    @Autowired
    private ISequenceManager sequenceManager;

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping("/staff/module/{moduleId}/sequence/{id}")
    public String showSequence(@PathVariable("id") String id, @PathVariable("moduleId") String moduleId, Model model) {   
        System.out.println("inside controller??? ");
        model.addAttribute("module", moduleManager.getModule(moduleId));
        model.addAttribute("sequence", sequenceManager.getSequence(id));
        model.addAttribute("slidesOfSequence", sequenceManager.getSequence(id).getSlides());
             
        return "staff/module";
    }
}
