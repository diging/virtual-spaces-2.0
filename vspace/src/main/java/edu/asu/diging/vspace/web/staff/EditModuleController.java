package edu.asu.diging.vspace.web.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.web.staff.forms.ModuleForm;

@Controller
public class EditModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value = "/staff/module/{moduleId}/edit", method = RequestMethod.GET)
    public String show(Model model, @PathVariable("moduleId") String moduleId) {
        IModule module = moduleManager.getModule(moduleId);
        if(module!=null)
        {
        ModuleForm moduleForm = new ModuleForm();
        moduleForm.setName(module.getName());
        moduleForm.setDescription(module.getDescription());
        model.addAttribute("moduleForm", moduleForm);
        model.addAttribute("moduleId", moduleId);
        return "staff/modules/edit";
        }
        else
        {
            return "badrequest";
        }
    }

    @RequestMapping(value = "/staff/module/{moduleId}/edit", method = RequestMethod.POST)
    public String save(@ModelAttribute ModuleForm moduleForm, @PathVariable("moduleId") String moduleId) {
        IModule module = moduleManager.getModule(moduleId);
        if(module!=null)
        {
        module.setName(moduleForm.getName());
        module.setDescription(moduleForm.getDescription());
        moduleManager.storeModule(module);
        return "redirect:/staff/module/{moduleId}";
        }
        else
        {
            return "redirect:/404";
        }
    }

}
