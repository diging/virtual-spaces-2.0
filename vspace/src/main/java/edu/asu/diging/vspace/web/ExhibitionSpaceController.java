package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionSpaceController {
	
	@Autowired
	private ISpaceManager spaceManager;
	
	@Autowired
	private ISpaceDisplayManager spaceDisplayManager;
	
	@Autowired
	private ILinkManager linkManager;
	
	@RequestMapping(value="/exhibit/space/{id}")
	public String space(@PathVariable("id") String id, Model model) {
		ISpace space = spaceManager.getSpace(id);
		model.addAttribute("space", space);
		model.addAttribute("spaceLinks", linkManager.getSpaceLinkDisplays(id));
		model.addAttribute("externalLinkList", linkManager.getExternalLinkDisplays(id));
		model.addAttribute("display", spaceDisplayManager.getBySpace(space));
		
		return "space";
	}
}
