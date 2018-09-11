package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.data.DefaultExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.DefaultExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Controller
public class ExhibitionConfigurationController {

	@Autowired
	private SpaceRepository spaceRepo;
	@Autowired
	private SpaceManager spaceManager;
	@Autowired
	private DefaultExhibitionManager exhibitManager;
	@Autowired
	private DefaultExhibitionRepository exhibitRepo;
	
	@RequestMapping("/staff/exhibit/econfig")
	public String listSpaces(Model model) {
		if(exhibitRepo.count()>0) {
		model.addAttribute("exhibit", exhibitRepo.findAll());
		}
		model.addAttribute("spaces", spaceRepo.findAll());
		return "staff/exhibit/econfig";
	}
	
	@RequestMapping(value = "/staff/exhibit/config_add", method = RequestMethod.POST)
	public String addDefaultSpace(@RequestParam(required=false,name="dexhibit") String exhibitID, @RequestParam("dspace") String spaceID) throws IOException {
		DefaultExhibition exhibit;
		Space space = (Space) spaceManager.getSpace(spaceID);
		if(exhibitID.equals("New"))
			exhibit = new DefaultExhibition();
		else
			exhibit = (DefaultExhibition)exhibitManager.getExhibitionbyId(exhibitID);
		exhibit.setSpace(space);
		exhibitManager.storeSpace(exhibit);
		return "redirect:/staff/space/list";
	}
}
