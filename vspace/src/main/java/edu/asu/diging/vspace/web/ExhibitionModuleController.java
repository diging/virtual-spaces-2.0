package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class ExhibitionModuleController {

	@Autowired
	private IModuleManager moduleManager;

	@RequestMapping(value = "/exhibit/module/{id}")
	public String module(@PathVariable("id") String id, Model model) {
		IModule module = moduleManager.getModule(id);
		model.addAttribute("module", module);
		if (module.getStartSequence() == null) {
			model.addAttribute("error","Sorry, this module has not been configured yet.");
			return "module";
		} 
		String startSequenceID=module.getStartSequence().getId();
		return "redirect:/exhibit/module/"+id+"/sequence/"+startSequenceID;
	}
}
