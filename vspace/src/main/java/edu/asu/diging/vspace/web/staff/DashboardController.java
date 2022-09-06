package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.impl.ExhibitionFactory;
import edu.asu.diging.vspace.core.model.ExhibitionModes;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;

import edu.asu.diging.simpleusers.core.model.Role;

@Controller
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExhibitionManager exhibitManager;

    @RequestMapping("/staff/dashboard")
    public String displayDashboard(Model model, Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(Role.ADMIN))) {
            model.addAttribute("spaceLinksSize", spaceLinkManager.getCountOfSpaceLinksWithSourceNull());
        }
        List<Space> recentSpaces = spaceRepo.findTop5ByOrderByCreationDateDesc();
        List<Module> recentModules = moduleRepo.findTop5ByOrderByCreationDateDesc();

        Exhibition exhibition = (Exhibition) exhibitManager.getStartExhibition();
        String previewId = null;
        ExhibitionModes exhibitionMode = null;
        if (exhibition != null) {
            previewId = exhibition.getPreviewId();
            exhibitionMode = exhibition.getMode();
        }
        model.addAttribute("recentSpaces", recentSpaces);
        model.addAttribute("recentModules", recentModules);
        model.addAttribute("previewId", previewId);
        model.addAttribute("exhibitionMode", exhibitionMode!=null?exhibitionMode.name():null);
        return "staff/dashboard/dashboard";
    }

}