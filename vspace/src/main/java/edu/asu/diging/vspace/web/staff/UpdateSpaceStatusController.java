package edu.asu.diging.vspace.web.staff;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpacesCustomOrderManager;


@Controller
public class UpdateSpaceStatusController {
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private ISpacesCustomOrderManager spaceCustomOrderManager;
    
    @RequestMapping(value="/staff/space/{spaceId}/status", method=RequestMethod.POST)
    public String updateStatus(HttpServletRequest request,RedirectAttributes attributes, @PathVariable("spaceId") String spaceId, @RequestParam("statusParam") SpaceStatus status) {
        ISpace space = spaceManager.getSpace(spaceId);
        space.setSpaceStatus(status);
        spaceManager.storeSpace(space, null,null);
        spaceCustomOrderManager.updateStatusChange(space, status);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Status successfully updated!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/space/{spaceId}";
    }
}
