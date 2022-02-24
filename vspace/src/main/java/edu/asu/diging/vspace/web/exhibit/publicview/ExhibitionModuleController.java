package edu.asu.diging.vspace.web.exhibit.publicview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IExhibitionAboutPageManager aboutPageManager;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{id}")
    public String module(@PathVariable("id") String id, @PathVariable("spaceId") String spaceId, Model model)
            throws SpaceNotFoundException, ModuleNotFoundException {
        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            return "redirect:/exhibit/404";
        }
        ExhibitionAboutPage exhibitionAboutPage = aboutPageManager.getExhibitionAboutPage();
        model.addAttribute("aboutPageTitle", exhibitionAboutPage.getTitle());
        model.addAttribute("aboutPageText", exhibitionAboutPage.getAboutPageText()); 
        IModule module = moduleManager.getModule(id);
        model.addAttribute("module", module);
        if (module == null) {
            return "redirect:/exhibit/404";
        } else if (module.getStartSequence() == null) {
            model.addAttribute("showAlert", true);
            model.addAttribute("message", "Sorry, module has not been configured yet.");
            return "/exhibition/module";
        }
        String startSequenceID = module.getStartSequence().getId();
        return "redirect:/exhibit/{spaceId}/module/" + id + "/sequence/" + startSequenceID;
    }
}