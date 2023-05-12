package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class ModuleController {

    public static final String STAFF_MODULE_PATH = "/staff/module/";

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private IModuleLinkManager moduleLink;

    @RequestMapping(STAFF_MODULE_PATH+"{id}")
    public String showModule(@PathVariable String id, Model model) {
        IModule module = moduleManager.getModule(id);
        model.addAttribute("module", module);
        model.addAttribute("slides", moduleManager.getModuleSlides(id));
        model.addAttribute("sequences", moduleManager.getModuleSequences(id));
        model.addAttribute("moduleStatus", module.getModuleStatus());
        model.addAttribute("spacesList", moduleLink.findSpaceListFromModuleId(id));
        return "staff/modules/module";
    }
}
