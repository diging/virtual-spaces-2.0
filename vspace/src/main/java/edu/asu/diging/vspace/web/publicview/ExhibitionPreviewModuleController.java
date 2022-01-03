package edu.asu.diging.vspace.web.publicview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ExhibitionPreviewModuleController {

	@Autowired
	private IModuleManager moduleManager;

	@Autowired
	private ISpaceManager spaceManager;

	@Autowired
	private IExhibitionManager exhibitionManager;

	@RequestMapping(value = "/exhibition/preview/{spaceId}/module/{id}")
	public String module(@PathVariable("id") String id, @PathVariable("spaceId") String spaceId, Model model)
			throws SpaceNotFoundException, ModuleNotFoundException {

		model.addAttribute("isExhPreview", true);
		Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
		model.addAttribute("PreviewId", exhibition.getPreviewId());
		ISpace space = spaceManager.getSpace(spaceId);
		if (space == null) {
			return "/exhibition/badrequest";
		}
		IModule module = moduleManager.getModule(id);
		model.addAttribute("module", module);
		if (module == null) {
			return "/exhibition/badrequest";
		} else if (module.getStartSequence() == null) {
			model.addAttribute("showAlert", true);
			model.addAttribute("message", "Sorry, module has not been configured yet.");
			return "/exhibition/module";
		}
		String startSequenceID = module.getStartSequence().getId();
		return "redirect:/exhibition/preview/{spaceId}/module/" + id + "/sequence/" + startSequenceID;
	}
}
