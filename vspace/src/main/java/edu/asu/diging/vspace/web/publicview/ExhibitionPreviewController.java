package edu.asu.diging.vspace.web.publicview;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;

@Controller
public class ExhibitionPreviewController {
    @Autowired
    private ISpaceManager spaceManager;

    @Autowired
    private ISpaceDisplayManager spaceDisplayManager;

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;

    @Autowired
    private SequenceHistory sequenceHistory;

    @RequestMapping(value = "/exhibition/preview/{previewId}")
    public String space(@PathVariable("previewId") String previewId, Model model) {

        model.addAttribute("isExhPreview", true);
        Exhibition exhibition = (Exhibition) exhibitionManager.getStartExhibition();
        if (exhibition.getPreviewId() == null || !exhibition.getPreviewId().equals(previewId)) {
            return "/exhibition/badrequest";
        }
        model.addAttribute("PreviewId", exhibition.getPreviewId());
        ExhibitionModes exhibitionMode = exhibition.getMode();
        if (!exhibitionMode.equals(ExhibitionModes.ACTIVE)) {
            String id = exhibition.getStartSpace().getId();

            ISpace space = spaceManager.getSpace(id);
            List<ISpaceLinkDisplay> spaceLinks;
            /*
             * (non-Javadoc) Below null check is added to accommodate already existing
             * spaces with null space status
             */
            if (space.getSpaceStatus() == null || space.getSpaceStatus().equals(SpaceStatus.PUBLISHED)) {
                model.addAttribute("exhibitionConfig", exhibition);
                model.addAttribute("space", space);
                model.addAttribute("moduleList", moduleLinkManager.getLinkDisplays(id));
                if (space.isShowUnpublishedLinks()) {
                    spaceLinks = spaceLinkManager.getLinkDisplays(id);
                } else {
                    spaceLinks = spaceLinkManager.getSpaceLinkForGivenOrNullSpaceStatus(id, SpaceStatus.PUBLISHED);
                }
                List<ISpaceLinkDisplay> filteredSpaceLinks = spaceLinks.stream().filter(
                        spaceLinkDisplayObj -> !spaceLinkDisplayObj.getLink().getTargetSpace().isHideIncomingLinks())
                        .collect(Collectors.toList());
                model.addAttribute("spaceLinks", filteredSpaceLinks);
                model.addAttribute("display", spaceDisplayManager.getBySpace(space));
                model.addAttribute("externalLinkList", externalLinkManager.getLinkDisplays(id));
            } else {
                return "/exhibition/badrequest";
            }

            if (sequenceHistory.hasHistory()) {
                sequenceHistory.flushFromHistory();
            }
            return "exhibition/space";
        } else {
            model.addAttribute("modeValue", "This Exhibition is Active.Use this Link for non-active Exhibition");
            return "/exhibition/maintenance";
        }

    }
}
