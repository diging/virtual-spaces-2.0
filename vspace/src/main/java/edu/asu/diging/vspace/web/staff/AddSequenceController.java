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

import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class AddSequenceController {

    @Autowired
    private IModuleManager moduleManager;
    
    @RequestMapping(value = "/staff/module/{id}/sequence/", method = RequestMethod.POST)
    public String addSlide(@PathVariable("id") String id, @RequestParam("seqTitle") String title, @RequestParam("seqDescription") String description, Model model, @RequestParam("file") MultipartFile file,
            Principal principal, RedirectAttributes attributes) throws IOException {
        //ISequence sequence = moduleManager.createSequence(id,title,description);
        return "redirect:/staff/module/{id}";
    }
}
