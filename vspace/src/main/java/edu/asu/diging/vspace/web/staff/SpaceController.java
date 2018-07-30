package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class SpaceController {
	
	@Autowired
	private ISpaceManager spaceManager;

	@RequestMapping("/staff/space/{id}")
	public String showSpace(@PathVariable String id, Model model) {
		ISpace space = spaceManager.getSpace(id);
		model.addAttribute("space", space);
		
		return "staff/space";
	}
}
