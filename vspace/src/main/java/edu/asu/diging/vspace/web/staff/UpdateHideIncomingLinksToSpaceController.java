package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class UpdateHideIncomingLinksToSpaceController {
	@Autowired
    private ISpaceManager spaceManager;
	
	@RequestMapping(value="/staff/space/links/edit/{spaceId}/hideSpaceLinksFromPublishedSpace", method=RequestMethod.POST)
    public String updateStatusForPublishedSpace(RedirectAttributes attributes, @PathVariable("spaceId") String spaceId,
    		@RequestParam("hideSpaceLinksPublishedSpaceParam") Boolean hideAllIncomingLinksToGivenSpace) {
	    System.out.println("Inside UpdateHideIncomingLinksToSpaceController --->");
		ISpace space = spaceManager.getSpace(spaceId);
		space.setHideAllIncomingLinksToGivenSpace(hideAllIncomingLinksToGivenSpace);
		spaceManager.storeSpace(space, null, null);
		attributes.addAttribute("alertType", "success");
		attributes.addAttribute("message", "Hide all space links to this Space successfully updated!");
		attributes.addAttribute("showAlert", "true");
		return "redirect:/staff/space/{spaceId}";
    }

}
