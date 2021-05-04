package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;

@Controller
public class ExhibitionAboutPageController {

    @Autowired
    private IExhibitionAboutPageManager exhibitAbtPageManager;

    /**
     * 
     * @param model
     * @return
     */
    
    @RequestMapping("/staff/exhibit/aboutpage")
    public String showAboutPage(Model model) {
        
        ExhibitionAboutPage exhibitionAboutPage = null;
        if (null != exhibitAbtPageManager.findAll() && !exhibitAbtPageManager.findAll().isEmpty()) {
            exhibitionAboutPage = exhibitAbtPageManager.findAll().get(0);
        }
        if (null != exhibitionAboutPage) {
            model.addAttribute("exhiAbtPage", exhibitionAboutPage);
        } else {
            model.addAttribute("exhiAbtPage", new ExhibitionAboutPage());
        }
        return "staff/exhibit/aboutPage";
    }
    /**
     * 
     * @param request
     * @param title
     * @param aboutPageText
     * @param attributes
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/staff/exhibit/aboutpage", method = RequestMethod.POST)
    public RedirectView createOrUpdateExhibition(HttpServletRequest request, @RequestParam("title") String title,
            @RequestParam("aboutPageText") String aboutPageText, RedirectAttributes attributes) throws IOException {
               
        ExhibitionAboutPage exhibitionAboutPage = null;
        if (null != exhibitAbtPageManager.findAll() && !exhibitAbtPageManager.findAll().isEmpty()) {
            exhibitionAboutPage = exhibitAbtPageManager.findAll().get(0);
        }
        else
        {
            exhibitionAboutPage= new ExhibitionAboutPage();
        }
        exhibitionAboutPage.setTitle(title);
        exhibitionAboutPage.setAboutPageText(aboutPageText);
        exhibitionAboutPage = exhibitAbtPageManager.storeExhibitionAbtPage(exhibitionAboutPage);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        return new RedirectView(request.getContextPath() + "/staff/exhibit/aboutpage");
    }

}
