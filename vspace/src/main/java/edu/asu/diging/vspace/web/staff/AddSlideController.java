package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.services.ISlideManager;
import edu.asu.diging.vspace.core.services.impl.ModuleManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Controller
public class AddSlideController {

    @Autowired
    private ISlideManager slideManager;
    
    @Autowired
    private ModuleManager moduleManager;

    @RequestMapping(value = "/staff/module/{id}/slide/add", method = RequestMethod.GET)
    public String showAddSlide(@PathVariable("id") String moduleId, Model model) {
        model.addAttribute("moduleId", moduleId);
        model.addAttribute("slide", new SlideForm());
        model.addAttribute("sequences", moduleManager.getModuleSequences(moduleId));
        
        return "staff/module/slide/add";
    }

    @RequestMapping(value = "/staff/module/{moduleId}/slide/add", method = RequestMethod.POST)
    public String addSlide(Model model, @PathVariable("moduleId") String moduleId, @ModelAttribute SlideForm slideForm,
            Principal principal) {

        IModule module = moduleManager.getModule(moduleId);
        SlideType type = slideForm.getType().isEmpty() ? null : SlideType.valueOf(slideForm.getType());
        if(type.equals(SlideType.BRANCHINGPOINT)) {
            slideManager.createBranchingPoint(module, slideForm, type);           
        } else {
            slideManager.createSlide(module, slideForm, type);
        }
        
        return "redirect:/staff/module/{moduleId}";
    }
}