package edu.asu.diging.vspace.web.staff;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.impl.ExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SpaceManager;

@Controller
public class ExhibitionConfigurationController {

	@Autowired
	private SpaceRepository spaceRepo;
	@Autowired
	private SpaceManager spaceManager;
	@Autowired
	private ExhibitionManager exhibitManager;
	@Autowired
	private ExhibitionRepository exhibitRepo;
	@Autowired
	private ExhibitionFactory exhibitFactory;
	@RequestMapping("/staff/exhibit/config")
	public String showExhibitions(Model model) {
		model.addAttribute("exhibit",exhibitRepo.findAll());
		model.addAttribute("spaces", spaceRepo.findAll());
		return "staff/exhibit/config";
	}
	
	@RequestMapping(value = "/staff/exhibit/config", method = RequestMethod.POST)
	public String addDefaultSpace(@RequestParam(required=false,name="dexhibit") String exhibitID, @RequestParam("dspace") String spaceID) throws IOException {
		Exhibition exhibit;
		Space space = (Space) spaceManager.getSpace(spaceID);
		exhibit = (Exhibition)exhibitFactory.createSpace(exhibitID);
		exhibit.setSpace(space);
		exhibitManager.storeExhibition(exhibit);
		return "redirect:/staff/exhibit/config";
	}
}
