package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

/**
 * 
 * @author Avirup Biswas
 *
 */
@Controller
public class ExhibitionAboutPageController {

    @Autowired
    private IExhibitionAboutPageManager aboutPageManager;

    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.GET)
    public String showAboutPage(Model model) {
        List<ExhibitionAboutPage> aboutPageList = aboutPageManager.findAll();
        ExhibitionAboutPage exhibitionAboutPage = aboutPageList != null && !aboutPageList.isEmpty() ? aboutPageList.get(0):new ExhibitionAboutPage();
        model.addAttribute("aboutPage", exhibitionAboutPage);
        return "staff/exhibit/aboutPage";
    }

    
    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.POST)
    public String createOrUpdateAboutPage(@ModelAttribute ExhibitionAboutPage aboutPageForm, RedirectAttributes attributes) throws IOException {
        List<ExhibitionAboutPage> aboutPageList = aboutPageManager.findAll();
        ExhibitionAboutPage exhibitionAboutPage = aboutPageList != null && !aboutPageList.isEmpty() ? aboutPageList.get(0):new ExhibitionAboutPage();
        exhibitionAboutPage.setTitle(aboutPageForm.getTitle());
        exhibitionAboutPage.setAboutPageText(aboutPageForm.getAboutPageText());
        exhibitionAboutPage = aboutPageManager.store(exhibitionAboutPage);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/exhibit/about";
    }

}
