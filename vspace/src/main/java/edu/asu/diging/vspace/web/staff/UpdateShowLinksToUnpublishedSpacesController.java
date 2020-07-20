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
import edu.asu.diging.vspace.core.model.impl.ShowSpaceLinksToUnpublishedSpaces;
import edu.asu.diging.vspace.core.services.ISpaceManager;


@Controller
public class UpdateShowLinksToUnpublishedSpacesController {
    @Autowired
    private ISpaceManager spaceManager;

    @RequestMapping(value="/staff/space/{spaceId}/showSpaceLinks", method=RequestMethod.POST)
    public String updateStatus(HttpServletRequest request,RedirectAttributes attributes, @PathVariable("spaceId") String spaceId, @RequestParam("showSpaceLinksParam") ShowSpaceLinksToUnpublishedSpaces showSpaceLinksToUnpublishedSpaces) {
        ISpace space = spaceManager.getSpace(spaceId);
        space.setShowSpaceLinksToUnpublishedSpaces(showSpaceLinksToUnpublishedSpaces);
        spaceManager.storeSpace(space, null,null);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Show space links to Unpublished Spaces successfully updated!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/space/{spaceId}";
    }
}