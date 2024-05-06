package edu.asu.diging.vspace.web.exhibit.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.core.exception.SpaceNotFoundException;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.model.impl.ModuleStatus;

@Controller
public class ExhibitionModuleController {

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private IExhibitionManager exhibitManager;

    @RequestMapping(value = { "/exhibit/{spaceId}/module/{id}", "/preview/{"+ExhibitionConstants.PREVIEW_ID+"}/{spaceId}/module/{id}" })
    public String module(@PathVariable("id") String id, @PathVariable("spaceId") String spaceId,
            @PathVariable(name = ExhibitionConstants.PREVIEW_ID, required = false) String previewId, Model model)
            throws SpaceNotFoundException, ModuleNotFoundException {

        ISpace space = spaceManager.getSpace(spaceId);
        if (space == null) {
            return "redirect:/exhibit/404";
        } 
        IModule module = moduleManager.getModule(id);       
        model.addAttribute("module", module);
        if (module == null || module.getModuleStatus() == ModuleStatus.UNPUBLISHED) {
            return "redirect:/exhibit/404";
        } else if (module.getStartSequence() == null) {
            model.addAttribute("showAlert", true);
            model.addAttribute("message", "Sorry, module has not been configured yet.");
            return "/exhibition/module";
        }
        IExhibition exhibition = exhibitManager.getStartExhibition();
        model.addAttribute("exhibitionConfig", exhibition);
        String startSequenceID = module.getStartSequence().getId();
        if (previewId != null) {
            return "redirect:/preview/" + previewId + "/" + spaceId + "/module/" + id + "/sequence/" + startSequenceID;
        } else {
            return "redirect:/exhibit/" + spaceId + "/module/" + id + "/sequence/" + startSequenceID;
        }
    }
}
