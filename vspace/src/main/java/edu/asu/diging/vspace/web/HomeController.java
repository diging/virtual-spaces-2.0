package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISetupManager;

@Controller
public class HomeController {

    @Autowired
    private IExhibitionManager exhibitionManager;

    @Autowired
    private ISetupManager setupManager;

    @RequestMapping(value = "/")
    public String home(Model model) {
        if (!setupManager.isSetup()) {
            return "setup";
        }

        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if (exhibition != null && exhibition.getStartSpace() != null) {
            return "redirect:/exhibit/space/" + exhibition.getStartSpace().getId();
        }
        return "home";
    }
    @RequestMapping(value ="/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/403") 
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping(value="/exhibit/404")
    public String error404(){
        return "badrequest";
    }

    @RequestMapping(value="/404")
    public String badRequest(){
        return "badrequest";
    }
}
