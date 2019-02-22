package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.impl.ModuleManager;

@Controller
public class SlideController {

    @Autowired
    private ModuleManager moduleManager;
    
    @RequestMapping("/staff/module/slide/{id}")
    public String listSpaces(@PathVariable String id, Model model) {
        
        model.addAttribute("slide", moduleManager.getSlide(id));
        
        
        return "staff/module/slide";
    }
}
