package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.data.SpaceRepository;
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
	
	@RequestMapping(value="/staff/space/add", method=RequestMethod.GET)
	public String showAddSpace(Model model) {
		model.addAttribute("space", new SpaceForm());
		
		return "staff/space/add";
	}
	
	@RequestMapping(value="/staff/space/add", method=RequestMethod.POST)
	public String addSpace(Model model, @ModelAttribute SpaceForm spaceForm, Principal principal) {
		
		ISpace space = spaceFactory.createSpace(spaceForm);
		spaceManager.storeSpace(space, principal.getName());
		
		return "redirect:/staff/space/list";
	}
}
