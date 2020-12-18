package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class EditSpaceController {

    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.GET)
    public String show(Model model, @PathVariable("spaceId") String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);
        SpaceForm spaceForm = new SpaceForm();
        spaceForm.setName(space.getName());
        spaceForm.setDescription(space.getDescription());
        model.addAttribute("spaceForm", spaceForm);
        model.addAttribute("spaceId", spaceId);
        
        return "staff/spaces/edit";
    }
    
    @RequestMapping(value="/staff/space/{spaceId}/edit", method=RequestMethod.POST)
    public String save(@ModelAttribute SpaceForm spaceForm, @PathVariable("spaceId") String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);
        space.setName(spaceForm.getName());
        space.setDescription(spaceForm.getDescription());
        
        spaceManager.storeSpace(space, null, null);
        return "redirect:/staff/space/{spaceId}";
    }
}
