package edu.asu.diging.vspace.web.exhibit.view;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.auth.IAuthenticationFacade;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.SequenceHistory;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.core.services.ISpaceTextBlockManager;
import edu.asu.diging.vspace.core.services.ILanguageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;

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
    
    @Autowired
    private ISpaceTextBlockManager spaceTextBlockManager;
    
    @Autowired
    private ILanguageService languageService;

    @RequestMapping(value = { "/exhibit/space/{id}", "/preview/{"+ExhibitionConstants.PREVIEW_ID+"}/space/{id}" })
    public String space(@PathVariable("id") String id, 
                       @RequestParam(value = "lang", required = false) String languageCode,
                       HttpServletRequest request, Model model) {
        ISpace space = spaceManager.getSpace(id);
        List<ISpaceLinkDisplay> spaceLinks;
        Boolean isSpacePublished = true;
        /* (non-Javadoc)
         * Below null check is added to accommodate already existing spaces with null space status
         */
        if (space.getSpaceStatus() != null && space.getSpaceStatus().equals(SpaceStatus.UNPUBLISHED)) {
            if (authenticationFacade.getAuthenticatedUser() != null) {
                isSpacePublished = false;
            } else {
                return "redirect:/exhibit/404";
            }
        }
        model.addAttribute("isSpacePublished", isSpacePublished);
        IExhibition exhibition = exhibitManager.getStartExhibition();
        model.addAttribute("exhibitionConfig", exhibition);
        
        // Handle language selection
        HttpSession session = request.getSession();
        if (languageCode != null) {
            session.setAttribute("selectedLanguage", languageCode);
        } else {
            languageCode = (String) session.getAttribute("selectedLanguage");
            if (languageCode == null) {
                languageCode = languageService.getDefaultLanguageCode();
            }
        }

        String localizedSpaceName = languageService.getLocalizedText(space.getSpaceNames(), languageCode, languageService.getDefaultLanguageCode());
        String localizedSpaceDescription = languageService.getLocalizedText(space.getSpaceDescriptions(), languageCode, languageService.getDefaultLanguageCode());
        
        if (!localizedSpaceName.isEmpty()) {
            space.setName(localizedSpaceName);
        }
        if (!localizedSpaceDescription.isEmpty()) {
            space.setDescription(localizedSpaceDescription);
        }
        
        model.addAttribute("space", space);
        model.addAttribute("selectedLanguage", languageCode);
        model.addAttribute("availableLanguages", languageService.getAvailableLanguages());
        model.addAttribute("moduleList", moduleLinkManager.getLinkDisplays(id));
        model.addAttribute("spaceTextBlocks", spaceTextBlockManager.getSpaceTextBlockDisplays(id));
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

        if (sequenceHistory.hasHistory()) {
            sequenceHistory.flushFromHistory();
        }
        return "exhibition/space";
    }
}