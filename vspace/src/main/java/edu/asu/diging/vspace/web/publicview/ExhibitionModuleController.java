package edu.asu.diging.vspace.web.publicview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.IModuleManager;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @RequestMapping(value = "/exhibit/module/{id}")
    public String module(@PathVariable("id") String id, Model model) {
        model.addAttribute("module", moduleManager.getModule(id));

        return "module";
    }
}
