package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class ModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping("/staff/module/{id}")
    public String showModule(@PathVariable String id, Model model) {
        ModuleStatus status = moduleManager.getModule(id).getModuleStatus();
        model.addAttribute("module", moduleManager.getModule(id));
        model.addAttribute("slides", moduleManager.getModuleSlides(id));
        model.addAttribute("sequences", moduleManager.getModuleSequences(id));
        model.addAttribute("moduleStatus", status);
        return "staff/modules/module";
    }
}
