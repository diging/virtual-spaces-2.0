package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class AddSpaceController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceFactory spaceFactory;

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.GET)
    public String showAddSpace(Model model) {
        model.addAttribute("space", new SpaceForm());

        return "staff/space/add";
    }

    @RequestMapping(value = "/staff/space/add", method = RequestMethod.POST)
    public String addSpace(Model model, @ModelAttribute SpaceForm spaceForm, @RequestParam("file") MultipartFile file,
            Principal principal) throws IOException {

        ISpace space = spaceFactory.createSpace(spaceForm);
        byte[] bgImage = null;
        String filename = null;
        if (file != null) {
            bgImage = file.getBytes();
            filename = file.getOriginalFilename();
        }
        spaceManager.storeSpace(space, bgImage, filename);

        return "redirect:/staff/space/list";
    }

    @RequestMapping(value = "/staff/space/update/{id}", method = RequestMethod.POST)
    public String updateSpace(HttpServletRequest request, @PathVariable("id") String id, Model model,
            @RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes attributes)
            throws IOException {

        byte[] bgImage = null;
        String filename = null;
        if (file.isEmpty() || file.equals(null)) {
            attributes.addAttribute("alertType", "warning");
            attributes.addAttribute("showAlert", "true");
            attributes.addAttribute("message", "Please Select a Background Image");
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
