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
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;

import edu.asu.diging.simpleusers.web.admin.DigingUserRoleConstants;

@Controller
public class DashboardController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;
    
    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @RequestMapping("/staff/dashboard")
    public String displayDashboard(Model model, Authentication authentication) {
        if(authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(DigingUserRoleConstants.ADMIN_ROLE))) {
            long spaceLinksSize = spaceLinkManager.getCountOfSpaceLinksWithSourceNull();
            model.addAttribute("admin", true);
            model.addAttribute("spaceLinksSize", spaceLinksSize);
        }
        List<Space> recentSpaces = spaceRepo.findTop5ByOrderByCreationDateDesc();
        List<Module> recentModules = moduleRepo.findTop5ByOrderByCreationDateDesc();
        model.addAttribute("recentSpaces", recentSpaces);
        model.addAttribute("recentModules", recentModules);
        return "staff/dashboard/dashboard";
    }

}