package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AddSlideController {
    
    @RequestMapping(value = "/staff/module/{id}/slide", method = RequestMethod.POST)
    public String addSlide(@PathVariable("id") String id, @RequestParam("slideTitle") String title, @RequestParam("slideDescription") String description, Model model, @RequestParam("file") MultipartFile file,
            Principal principal, RedirectAttributes attributes) throws IOException {
        
        System.out.println("in controller");
        
        return "redirect:/staff/module/{id}/slide";

    }
}
