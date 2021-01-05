package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.vspace.core.services.ISetupManager;

@Controller
public class SetupAdminController {
    
    @Autowired
    private ISetupManager setupManager;

    @RequestMapping(value="/setup/admin", method=RequestMethod.POST)
    public String setup(@RequestParam("adminpw") String password, RedirectAttributes redirectAttrs, Model model) {
        if (setupManager.isSetup()) {
            redirectAttrs.addFlashAttribute("showAlert", true);
            redirectAttrs.addFlashAttribute("alertType", "warning");
            redirectAttrs.addFlashAttribute("message", "Looks like the admin user has already been configured.");
            return "redirect:/";
        }
        
        try {
            IUser user = setupManager.setupAdmin(password);
            model.addAttribute("adminUsername", user.getUsername());
        } catch (UserAlreadyExistsException e) {
            redirectAttrs.addFlashAttribute("showAlert", true);
            redirectAttrs.addFlashAttribute("alertType", "warning");
            redirectAttrs.addFlashAttribute("message", "Looks like the admin user has already been configured.");
            return "redirect:/";
        }
        return "setupComplete";
    }
}
