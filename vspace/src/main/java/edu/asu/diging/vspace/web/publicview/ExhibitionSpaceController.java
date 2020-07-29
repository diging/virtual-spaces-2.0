package edu.asu.diging.vspace.web.publicview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionSpaceController {

    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceDisplayManager spaceDisplayManager;

    @Autowired
    private IExhibitionManager exhibitManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;

    @RequestMapping(value = "/exhibit/space/{id}")
    public String space(@PathVariable("id") String id, Model model) {
        ISpace space = spaceManager.getSpace(id);
        /* (non-Javadoc)
         * Below null check is added to accommodate already existing spaces with null space status
         */
        if(space.getSpaceStatus() == null || space.getSpaceStatus().equals(SpaceStatus.PUBLISHED)) {
            IExhibition exhibition = exhibitManager.getStartExhibition();
            model.addAttribute("exhibitionConfig", exhibition);
            model.addAttribute("space", space);
            model.addAttribute("moduleList", moduleLinkManager.getLinkDisplays(id));
            if(space.isShowUnpublishedLinks()) {
                model.addAttribute("spaceLinks",spaceLinkManager.getLinkDisplays(id));
            }else {
                model.addAttribute("spaceLinks", spaceLinkManager.getSpaceLinkForGivenOrNullSpaceStatus(id, SpaceStatus.PUBLISHED));
            }
            model.addAttribute("display", spaceDisplayManager.getBySpace(space));
            model.addAttribute("externalLinkList", externalLinkManager.getLinkDisplays(id));  
        }
        else {
            model.addAttribute("showAlert", true);
            model.addAttribute("alertType", "danger");
            model.addAttribute("messageType","invalidSpace");
            model.addAttribute("message", "Access Denied.");
        }
        return "space";
    }
}