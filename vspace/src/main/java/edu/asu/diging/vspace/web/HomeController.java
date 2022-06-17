package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISetupManager;
import edu.asu.diging.vspace.web.exhibit.view.ExhibitionConstants;

@Controller
public class HomeController {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISetupManager setupManager;

    @RequestMapping(value = { "/", "/preview/{" + ExhibitionConstants.PREVIEW_ID + "}" })
    public String home(Model model,
            @PathVariable(name = ExhibitionConstants.PREVIEW_ID, required = false) String previewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!setupManager.isSetup()) {
            return "setup";
        }

        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if (exhibition != null && exhibition.getStartSpace() != null) {
            if (previewId != null) {
                return "redirect:/preview/{previewId}/space/" + exhibition.getStartSpace().getId();
            } else {
                if (!(authentication instanceof AnonymousAuthenticationToken)) {
                    return "redirect:/staff/dashboard/";
                } else {
                    return "redirect:/exhibit/space/" + exhibition.getStartSpace().getId();
                }
            }
        }
        return "home";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/403")
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping(value = { "/404", "/exhibit/404" })
    public String error404() {
        return "badrequest";
    }
}
