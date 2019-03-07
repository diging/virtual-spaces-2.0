package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class AddSlideController {
       
    @Autowired
    private ISlideManager slideManager;
    
    private static String modId;
     
    @RequestMapping(value="/staff/module/{id}/slide/add", method=RequestMethod.GET)
    public String showAddSlide(@PathVariable("id") String moduleId, Model model) {
        model.addAttribute("slide", new SlideForm());
        modId = moduleId;
        return "staff/module/slide/add";
    }
    
    @RequestMapping(value="/staff/module/slide/add", method=RequestMethod.POST)
    public String addSlide(Model model, @ModelAttribute SlideForm slideForm, Principal principal) {
        
        ISlide slide = slideManager.createSlide(modId, slideForm);
        slideManager.storeSlide(slide);
        String redirect="redirect:/staff/module/"+modId;
        return redirect;
    }
}