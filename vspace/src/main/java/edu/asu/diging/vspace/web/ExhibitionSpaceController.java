package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionSpaceController {
	
	@Autowired
	private ISpaceManager spaceManager;
	
	@RequestMapping(value="/exhibit/space/{id}")
	public String space(@PathVariable("id") String id, Model model) {
		model.addAttribute("space", spaceManager.getSpace(id));
		model.addAttribute("spaceLinks", spaceManager.getSpaceLinkDisplays(id));
		
		return "space";
	}
}
