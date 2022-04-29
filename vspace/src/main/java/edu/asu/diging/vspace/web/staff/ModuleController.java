package edu.asu.diging.vspace.web.staff;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.ModuleManager;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ModuleController {

    @Autowired
    private IModuleManager moduleManager;
   
    
    @Autowired
    private IModuleLinkManager moduleLink;
    
    
    @RequestMapping("/staff/module/{id}")
    public String showModule(@PathVariable String id, Model model) {
        IModule module = moduleManager.getModule(id);
        model.addAttribute("module", module);
        model.addAttribute("slides", moduleManager.getModuleSlides(id));
        model.addAttribute("sequences", moduleManager.getModuleSequences(id));
        model.addAttribute("moduleStatus", module.getModuleStatus());        
        HashSet<ISpace> spaces=moduleLink.findModuleLinksFromModuleId(id);
        if (spaces.isEmpty()) {
            model.addAttribute("message", "Sorry, No spaces has been linked to this Module");          
        }
        else
        {
        	model.addAttribute("spacesList", spaces);     	
        }      
        return "staff/modules/module";
    }
}
