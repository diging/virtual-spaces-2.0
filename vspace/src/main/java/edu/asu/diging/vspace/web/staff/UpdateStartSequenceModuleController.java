package edu.asu.diging.vspace.web.staff;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;

@Controller
public class UpdateStartSequenceModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISequenceManager sequenceManager;

    @RequestMapping(value = "/staff/module/{moduleId}/sequence/start", method = RequestMethod.POST)
    public String updateModule(HttpServletRequest request,
            @PathVariable("moduleId") String moduleId,
            @RequestParam("sequenceParam") String sequenceId,RedirectAttributes attributes) {
        IModule module=moduleManager.getModule(moduleId);
        module.setStartSequence(sequenceManager.getSequence(sequenceId));
        moduleManager.storeModule(module);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Start sequence successfully updated!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/module/{moduleId}";
    }
}
