package edu.asu.diging.vspace.web.staff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddContentBlockController {

    @RequestMapping(value = "/staff/module/{moduleId}/slide/{id}", method = RequestMethod.GET)
    public String showSlideContentBlocks(@PathVariable("moduleId") String moduleId, @PathVariable("id") String slideId,
            Model model) {
        model.addAttribute("moduleId", moduleId);
        
        return "redirect:/staff/module/{moduleId}";
    }
}