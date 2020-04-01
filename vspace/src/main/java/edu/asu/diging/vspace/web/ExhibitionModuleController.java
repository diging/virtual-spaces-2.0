package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.web.exception.ModuleNotConfiguredException;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{id}")
    public String module(@PathVariable("id") String id, Model model)
            throws ModuleNotFoundException, ModuleNotConfiguredException {
        IModule module = moduleManager.getModule(id);
        model.addAttribute("module", module);
        if (module == null) {
            throw new ModuleNotFoundException(id);
        } else if (module.getStartSequence() == null) {
            throw new ModuleNotConfiguredException(id);
        }
        String startSequenceID = module.getStartSequence().getId();
        return "redirect:/exhibit/{spaceId}/module/" + id + "/sequence/" + startSequenceID;
    }
}
