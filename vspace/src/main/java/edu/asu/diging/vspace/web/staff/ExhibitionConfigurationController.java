package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.DefaultExhibitionFactory;
import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.DefaultExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;
import edu.asu.diging.vspace.web.staff.forms.DefaultExhibitionForm;

@Controller
public class ExhibitionConfigurationController {
	
	@Autowired
	private SpaceRepository spaceRepo;
	@Autowired
	private SpaceManager spaceManager;
	@Autowired
	private DefaultExhibitionManager exhibitManager;
	
	@Autowired
	private DefaultExhibitionFactory exhibitFactory;
	
	@RequestMapping("/staff/exhibit/econfig")
	public String listSpaces(Model model) {
		
		model.addAttribute("exhibit", new DefaultExhibitionForm());
		model.addAttribute("spaces", spaceRepo.findAll());
		return "staff/exhibit/econfig";
	}
	@RequestMapping(value = "/staff/exhibit/config_add", method = RequestMethod.POST)
	public String addDefaultSpace(Model model, @RequestParam("dspace") String spaceID, Principal principal) throws IOException {

		System.out.println("Default " + spaceID);
		//DefaultExhibition exhibit = exhibitFactory.createDefaultExhibition();
		DefaultExhibition exhibit = new DefaultExhibition(); 
		System.out.println("exhibit@@@" + exhibit.getCreationDate());
		
		Space space = (Space) spaceManager.getSpace(spaceID);
		//System.out.println("Default Name "+space.getName());
		exhibitManager = new DefaultExhibitionManager(); 
		if(spaceID!=null)
		{
			exhibit.setSpace(space);
			exhibitManager.storeSpace((DefaultExhibition)exhibit);
		}
		System.out.println("Default Name "+exhibit.getSpace());
		return "redirect:/staff/exhibit/econfig";
	}
}
