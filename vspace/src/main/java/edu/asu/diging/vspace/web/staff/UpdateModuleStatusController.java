package edu.asu.diging.vspace.web.staff;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;
import edu.asu.diging.vspace.core.services.IModuleManager;

public class UpdateModuleStatusController {  
    @Autowired
    private IModuleManager moduleManager;
    
    @RequestMapping(value="/staff/space/{spaceId}/status", method=RequestMethod.POST)
    public String updateStatus(HttpServletRequest request,RedirectAttributes attributes, @PathVariable("moduleId") String spaceId, @RequestParam("statusParam") ModuleStatus status) {
        IModule module = moduleManager.getModule(spaceId);
        module.setModuleStatus(status);
        moduleManager.storeModule(module);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Status successfully updated!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/space/{spaceId}";
    }

}
