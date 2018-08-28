package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Controller
public class ExhibitionConfigurationController {
	
	@Autowired
	private SpaceRepository spaceRepo;
	
	@RequestMapping("/staff/exhibit/econfig")
	public String listSpaces(Model model) {
		
		model.addAttribute("exhibit", spaceRepo.findAll());
		model.addAttribute("spaces", spaceRepo.findAll());
		return "staff/exhibit/econfig";
	}
	@RequestMapping(value = "/staff/exhibit/config_add", method = RequestMethod.POST)
	public String addDefaultSpace(Model model, @ModelAttribute SpaceForm spaceForm, @RequestParam("dspace") String spaceID) throws IOException {

		//spaceManager.storeSpace(space, bgImage, filename);
		System.out.println("Default "+spaceForm.getName() + spaceID);
		return "redirect:/staff/exhibit/econfig";
	}
}
