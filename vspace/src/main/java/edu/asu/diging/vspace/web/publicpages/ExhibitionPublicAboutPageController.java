package edu.asu.diging.vspace.web.publicpages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;

@Controller
public class ExhibitionPublicAboutPageController {
    
    private Logger logger = LoggerFactory.getLogger(ExhibitionPublicAboutPageController.class);
    
    @Autowired
    private IExhibitionAboutPageManager aboutPageManager;
    
    @RequestMapping(value = "/exhibit/about")
    public String showPublicAboutPage(Model model) {
        model.addAttribute("exhibitionAboutPage", aboutPageManager.getExhibitionAboutPage());
        model.addAttribute("aboutPageConfigured", true);
        return "exhibition/aboutPagePublic";
    }

}
