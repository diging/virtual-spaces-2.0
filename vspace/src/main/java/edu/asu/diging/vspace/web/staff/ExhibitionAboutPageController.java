package edu.asu.diging.vspace.web.staff;

import java.io.IOException;
import java.util.ArrayList;
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

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;


/**
 * 
 * @author Avirup Biswas
 *
 */
@Controller
public class ExhibitionAboutPageController {

    @Autowired
    private IExhibitionAboutPageManager aboutPageManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
   
    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.GET)
    public String showAboutPage(Model model) {    
        ExhibitionAboutPage exhibitionAboutPage = aboutPageManager.getExhibitionAboutPage(); 
        AboutPageForm aboutPageForm=new AboutPageForm();
        aboutPageForm.setAboutPageText(exhibitionAboutPage.getAboutPageText());
        aboutPageForm.setTitle(exhibitionAboutPage.getTitle());
        
        
        IExhibition startExhibtion = exhibitionManager.getStartExhibition();

        startExhibtion.getLanguages().forEach(language -> {


            ILocalizedText title = exhibitionAboutPage.getExhibitionTitles().stream()
                    .filter(exhibitionTitle ->  exhibitionTitle.getExhibitionLanguage().getId().equals(language.getId())).findAny().orElse(null);

            if(title != null) {
                aboutPageForm.getTitles().add(new LocalizedTextForm(title.getText(), title.getId(), title.getExhibitionLanguage().getId(), title.getExhibitionLanguage().getLabel() ));

            }else {
                aboutPageForm.getTitles().add(new LocalizedTextForm(null, null, language.getId(), language.getLabel() ));

            }


            ILocalizedText aboutPageText = exhibitionAboutPage.getExhibitionTextDescriptions().stream()
                    .filter(exhibitionText -> language.getId().equals(exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);



            if(aboutPageText!= null) {
                aboutPageForm.getAboutPageTexts().add(new LocalizedTextForm(aboutPageText.getText(), aboutPageText.getId(), aboutPageText.getExhibitionLanguage().getId(), aboutPageText.getExhibitionLanguage().getLabel()));

            }else {
                aboutPageForm.getAboutPageTexts().add(new LocalizedTextForm(null, null, language.getId(), language.getLabel()));

            }


        });
        model.addAttribute("aboutPage", aboutPageForm);

        return "staff/exhibit/aboutPage";
    }

    
    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.POST)
    public String createOrUpdateAboutPage(@ModelAttribute AboutPageForm aboutPageForm, RedirectAttributes attributes) throws IOException {
        ExhibitionAboutPage exhibitionAboutPage = aboutPageManager.getExhibitionAboutPage();       
        exhibitionAboutPage.setTitle(aboutPageForm.getTitle());
        exhibitionAboutPage.setAboutPageText(aboutPageForm.getAboutPageText());

        exhibitionAboutPage=aboutPageManager.storeAboutPageData(exhibitionAboutPage, aboutPageForm);
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/exhibit/about";
    }   

}
