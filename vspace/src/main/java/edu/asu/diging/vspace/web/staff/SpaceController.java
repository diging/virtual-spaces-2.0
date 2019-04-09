package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ILinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class SpaceController {
	
	@Autowired
	private ISpaceManager spaceManager;
	
	@Autowired
	private ILinkManager linkManager;
	
	@Autowired
	private ModuleRepository moduleRepo;

	@RequestMapping("/staff/space/{id}")
	public String showSpace(@PathVariable String id, Model model) {
		ISpace space = spaceManager.getFullyLoadedSpace(id);
		model.addAttribute("space", space);
		model.addAttribute("spaceLinks", linkManager.getSpaceLinkDisplays(id));
		model.addAttribute("externalLinks", linkManager.getExternalLinkDisplays(id));
		model.addAttribute("moduleLinks", linkManager.getModuleLinkDisplays(id));
		model.addAttribute("spaces", spaceManager.getAllSpaces());
		model.addAttribute("modules", moduleRepo.findAll());
		
		return "staff/space";
	}
}
