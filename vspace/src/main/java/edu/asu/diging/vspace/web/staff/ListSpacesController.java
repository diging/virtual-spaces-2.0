package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.SpaceRepository;

@Controller
public class ListSpacesController {
	
	@Autowired
	private SpaceRepository spaceRepo;
	
	@RequestMapping("/staff/space/list")
	public String listSpaces(Model model) {
		
		model.addAttribute("spaces", spaceRepo.findAll());
		
		return "staff/space/list";
	}
}
