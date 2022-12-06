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
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ILanguageDescriptionObjectManager;
import edu.asu.diging.vspace.core.services.impl.LanguageDescriptionObjectManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;


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
    
    @Autowired
    private ILanguageDescriptionObjectManager languageObjectManager;
    

   
    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.GET)
    public String showAboutPage(Model model) {
        List<ExhibitionAboutPage> aboutPageList = aboutPageManager.findAll();
        ExhibitionAboutPage exhibitionAboutPage = aboutPageList != null && !aboutPageList.isEmpty() ? aboutPageList.get(0):new ExhibitionAboutPage();
        AboutPageForm aboutPageForm=new AboutPageForm();
        aboutPageForm.setAboutPageText(exhibitionAboutPage.getAboutPageText());
        aboutPageForm.setTitle(exhibitionAboutPage.getTitle());
        System.out.println(exhibitionAboutPage.getExhibitionTitles());
        List<LanguageDescriptionObject> titleList = new ArrayList();
        List<LanguageDescriptionObject> textList = new ArrayList();
        for(ILanguageDescriptionObject titles:exhibitionAboutPage.getExhibitionTitles())
		{
        	titleList.add((LanguageDescriptionObject) titles);
		}
		for(ILanguageDescriptionObject texts:exhibitionAboutPage.getExhibitionTextDescriptions())
		{
			textList.add((LanguageDescriptionObject) texts);
		}
        System.out.println("list "+titleList);
        System.out.println("list "+textList);
        aboutPageForm.setTitles(titleList);
        aboutPageForm.setAboutPageTexts(textList);
        //aboutPageForm.setTitles(exhibitionAboutPage.getExhibitionTitles());
        //aboutPageForm.setAboutPageTexts(aboutPageForm.getAboutPageTexts());
        model.addAttribute("aboutPage", aboutPageForm);
        IExhibition startExhibtion = exhibitionManager.getStartExhibition();
        
        List<LanguageDescriptionObject> languageObjectList = new ArrayList();
        startExhibtion.getLanguages().forEach(exhibitionLanguage -> {
        	LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
            languageObject.setExhibitionLanguage((ExhibitionLanguage)exhibitionLanguage);
            languageObjectList.add(languageObject);
        });
        model.addAttribute("languageObjectList" , startExhibtion.getLanguages());
        return "staff/exhibit/aboutPage";
    }

    
    @RequestMapping(value = "/staff/exhibit/about", method = RequestMethod.POST)
    public String createOrUpdateAboutPage(@ModelAttribute ExhibitionAboutPage aboutPageForm, AboutPageForm languageAboutPage, RedirectAttributes attributes) throws IOException {
        List<ExhibitionAboutPage> aboutPageList = aboutPageManager.findAll();
        ExhibitionAboutPage exhibitionAboutPage = new ExhibitionAboutPage();

        
        exhibitionAboutPage.setTitle(languageAboutPage.getTitle());
        exhibitionAboutPage.setAboutPageText(languageAboutPage.getAboutPageText());
        exhibitionAboutPage=languageObjectManager.storeAboutPageData(aboutPageForm,languageAboutPage);
        List<ILanguageDescriptionObject> titleList = new ArrayList();
        List<ILanguageDescriptionObject> textList = new ArrayList();
        for(LanguageDescriptionObject titles:languageAboutPage.getTitles())
		{
        	titleList.add(titles);
		}
		for(ILanguageDescriptionObject texts:languageAboutPage.getAboutPageTexts())
		{
			textList.add(texts);
		}
		exhibitionAboutPage.setExhibitionTitles(titleList);
		exhibitionAboutPage.setExhibitionTextDescriptions(textList);
        aboutPageManager.store(exhibitionAboutPage);
        System.out.println(exhibitionAboutPage.getExhibitionTitles());
        attributes.addAttribute("alertType", "success");
        attributes.addAttribute("message", "Successfully Saved!");
        attributes.addAttribute("showAlert", "true");
        return "redirect:/staff/exhibit/about";
    }   

}
