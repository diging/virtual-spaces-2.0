package edu.asu.diging.vspace.web.publicview;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.impl.SequenceOverviewManager;
import edu.asu.diging.vspace.core.model.SequenceOverview;
import edu.asu.diging.vspace.web.exception.ModuleNotFoundException;
import edu.asu.diging.vspace.web.exception.SpaceNotFoundException;

@Controller
public class ExhibitionModuleController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IModuleManager moduleManager;

    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private SequenceOverviewManager sequenceOverviewManager;

    @RequestMapping(value = "/exhibit/{spaceId}/module/{id}")
    public String module(@PathVariable("id") String id, @PathVariable("spaceId") String spaceId, Model model)
            throws SpaceNotFoundException, ModuleNotFoundException {
        ISpace space = spaceManager.getSpace(spaceId);
        List<SequenceOverview> sequenceOverview = sequenceOverviewManager.showModuleMap(id);
        if(sequenceOverview != null) {
            model.addAttribute("sequences", sequenceOverview);
            for(SequenceOverview sequence :  sequenceOverview) {
                logger.info("sequence is {} ", sequence.getName());
            }
        }
        logger.info("sequence overview is {} ", sequenceOverview.get(0).getName());
        if (space == null) {
            return "redirect:/exhibit/404";
        }
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
