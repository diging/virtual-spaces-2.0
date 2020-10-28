package edu.asu.diging.vspace.web.publicview;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
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

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private SequenceHistory sequenceHistory;

    @RequestMapping(value = "/exhibit/space/{id}")
    public String space(@PathVariable("id") String id, Model model) {
        ISpace space = spaceManager.getSpace(id);
        /* (non-Javadoc)
         * Below null check is added to accommodate already existing spaces with null space status
         */
        if(space.getSpaceStatus() == null || space.getSpaceStatus().equals(SpaceStatus.PUBLISHED)
                || authenticationFacade.getAuthenticatedUser() != null) {
            IExhibition exhibition = exhibitManager.getStartExhibition();
            model.addAttribute("exhibitionConfig", exhibition);
            model.addAttribute("space", space);
            model.addAttribute("moduleList", moduleLinkManager.getLinkDisplays(id));
            if(space.isShowUnpublishedLinks()) {
                List<ISpaceLinkDisplay> spaceLinks = spaceLinkManager.getLinkDisplays(id);
                List<ISpaceLinkDisplay> filteredSpacelinks = filterLinks(spaceLinks);
                model.addAttribute("spaceLinks",filteredSpacelinks);
            }else {
            	List<ISpaceLinkDisplay> spaceLinks = spaceLinkManager.getSpaceLinkForGivenOrNullSpaceStatus(id, SpaceStatus.PUBLISHED);
                List<ISpaceLinkDisplay> filteredSpacelinks = filterLinks(spaceLinks);
                model.addAttribute("spaceLinks",filteredSpacelinks);
            }
            model.addAttribute("display", spaceDisplayManager.getBySpace(space));
            model.addAttribute("externalLinkList", externalLinkManager.getLinkDisplays(id));  
        }
        else {
            return "redirect:/exhibit/404";
        }

        if(sequenceHistory.hasHistory()) {
            sequenceHistory.flushFromHistory();
        }
        return "exhibition/space";
    }

	private List<ISpaceLinkDisplay> filterLinks(List<ISpaceLinkDisplay> spaceLinks) {
		// Logic to filter the space links with hide links status to true
		
		List<ISpaceLinkDisplay> linksToDisplay = new ArrayList<ISpaceLinkDisplay>();
		for (ISpaceLinkDisplay spaceLinkDisplayObj : spaceLinks) {
			if(!spaceLinkDisplayObj.getLink().getTargetSpace().isHideLinksToUnpublishedSpaces()) {
				linksToDisplay.add(spaceLinkDisplayObj);
			}	
		}
		return linksToDisplay;
	}
}