package edu.asu.diging.vspace.web.staff;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

@Controller
public class DeleteModuleController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value="/staff/module/{moduleId}/delete", method=RequestMethod.DELETE)
    public String deleteModule(@PathVariable("moduleId") String moduleId, Model model) {
        moduleManager.deleteModule(moduleId);
        logger.info("deleted module");
        return "redirect:/staff/dashboard/";
    }
}
