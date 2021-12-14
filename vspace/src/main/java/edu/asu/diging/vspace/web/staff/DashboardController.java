package edu.asu.diging.vspace.web.staff;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;

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
    public String displayDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer spaceLinksSize=0;
        if(authentication.getName().equals("admin")) {
            List<ISpaceLink> spaceLinks = spaceLinkManager.findSpaceLinksWithSourceNull();
            spaceLinksSize = spaceLinks.size();
        }
        List<Space> recentSpaces = spaceRepo.findTop5ByOrderByCreationDateDesc();
        List<Module> recentModules = moduleRepo.findTop5ByOrderByCreationDateDesc();
        model.addAttribute("recentSpaces", recentSpaces);
        model.addAttribute("recentModules", recentModules);
        model.addAttribute("authenticationName", authentication.getName());
        model.addAttribute("spaceLinksSize", spaceLinksSize);
        return "staff/dashboard/dashboard";
    }
    
    @DeleteMapping(value = "/staff/dashboard/deleteSpaceLinksWithNullSource")
    public ResponseEntity<String> deleteSpaceLinkWithNullSource(){
        try {
            spaceLinkManager.deleteSpaceLinksWithSourceAsNull();
        }
        catch (Exception exception) {
            logger.error("Could not delete Space Links with Source Space Null.", exception);
            return new ResponseEntity<String>("Sorry, unable to delete the Space Links with source as null.",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Ok", HttpStatus.OK);
    }

}