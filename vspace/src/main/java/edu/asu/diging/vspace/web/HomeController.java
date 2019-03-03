package edu.asu.diging.vspace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.services.IExhibitionManager;

@Controller
public class HomeController {
    
    @Autowired
    private IExhibitionManager exhibitionManager;

    @RequestMapping(value = "/")
    public String home(Model model) {
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
}
