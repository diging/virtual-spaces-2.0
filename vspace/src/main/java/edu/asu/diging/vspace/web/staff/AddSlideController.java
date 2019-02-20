package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.exception.ModuleDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class AddSlideController {
    
    @Autowired
    private IModuleManager moduleManager;
    
    @RequestMapping(value = "/staff/module/{id}/slide/", method = RequestMethod.POST)
    public String addSlide(@PathVariable("id") String id, @RequestParam("slideTitle") String title, @RequestParam("slideDescription") String description, Model model, @RequestParam("file") MultipartFile file,
            Principal principal, RedirectAttributes attributes) throws IOException, ModuleDoesNotExistException {
        
        ISlide slide = moduleManager.createSlide(id,title,description);
        byte[] image = null;
        String filename = null;        
        if (file.isEmpty() || file.equals(null)) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Please select an image for the Slide.");
            return "redirect:/staff/module/{id}";

        } else if (file != null) {
            image = file.getBytes();
            filename = file.getOriginalFilename();
        }
        moduleManager.storeSlide(slide, image, filename);
        return "redirect:/staff/module/{id}";
    }
}
