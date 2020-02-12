package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.factory.IModuleFactory;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

@Controller
public class AddModuleController {
	
	@Autowired
	private IModuleFactory moduleFactory;
	
	@Autowired
	private IModuleManager moduleManager;
	
	@Autowired
	private ISequenceManager sequenceManager;

	@RequestMapping(value="/staff/module/add", method=RequestMethod.GET)
	public String showAddModule(Model model) {
		model.addAttribute("module", new ModuleForm());
		
		return "staff/module/add";
	}
	
	@RequestMapping(value="/staff/module/add", method=RequestMethod.POST)
	public String addModule(Model model, @ModelAttribute ModuleForm moduleForm, Principal principal) {
		
		IModule module = moduleFactory.createModule(moduleForm);
		moduleManager.storeModule(module);
		return "redirect:/staff/module/list";
	}
	
	@RequestMapping(value = "/staff/module/{moduleId}/update/{sequenceId}", method = RequestMethod.PUT)
    public String updateModule(Model model, @PathVariable("moduleId") String moduleId, @PathVariable("sequenceId") String sequenceId, Principal principal) {
	    IModule module=moduleManager.getModule(moduleId);
        module.setStartSequence(sequenceManager.getSequence(sequenceId));
        moduleManager.storeModule(module);
        return "redirect:/staff/module/{moduleId}";
    }
}
