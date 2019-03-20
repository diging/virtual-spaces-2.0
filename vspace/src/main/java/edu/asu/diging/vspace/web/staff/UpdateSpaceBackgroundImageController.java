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

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class UpdateSpaceBackgroundImageController {

    @Autowired
    private ISpaceManager spaceManager;
    
    @RequestMapping(value = "/staff/space/update/{id}", method = RequestMethod.POST)
    public String updateSpace(@PathVariable("id") String id, Model model, @RequestParam("file") MultipartFile file,
            Principal principal, RedirectAttributes attributes) throws IOException {

        byte[] bgImage = null;
        String filename = null;
        if (file.isEmpty() || file.equals(null)) {
            attributes.addAttribute("alertType", "danger");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Please select a background image.");
            return "redirect:/staff/space/{id}";

        } else if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        ISpace currentSpace = spaceManager.getFullyLoadedSpace(id);
        spaceManager.storeSpace(currentSpace, bgImage, filename);

        return "redirect:/staff/space/{id}";
    }
}
