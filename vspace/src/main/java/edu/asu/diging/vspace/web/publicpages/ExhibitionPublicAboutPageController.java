package edu.asu.diging.vspace.web.publicpages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.exhibit.view.ExhibitionConstants;

@Controller
public class ExhibitionPublicAboutPageController {
    
    private Logger logger = LoggerFactory.getLogger(ExhibitionPublicAboutPageController.class);
    
    @Autowired
    private IExhibitionAboutPageManager aboutPageManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @RequestMapping(value = { "/exhibit/about", "/preview/{"+ExhibitionConstants.PREVIEW_ID+"}/about" })
    public String showPublicAboutPage(Model model,
            @PathVariable(name = ExhibitionConstants.PREVIEW_ID, required = false) String previewId) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        IExhibitionLanguage defaultLanguage = exhibitionManager.getDefaultLanguage(exhibition);
        String languageCode = defaultLanguage != null ? defaultLanguage.getCode() : "en";
        
        if (previewId != null) {
            return "redirect:/preview/{previewId}/about/" + languageCode;
        }
        return "redirect:/exhibit/about/" + languageCode;
    }
    
    @RequestMapping(value = { "/exhibit/about/{languageCode}", "/preview/{"+ExhibitionConstants.PREVIEW_ID+"}/about/{languageCode}" })
    public String showPublicAboutPageWithLanguage(Model model, @PathVariable("languageCode") String languageCode,
            @PathVariable(name = ExhibitionConstants.PREVIEW_ID, required = false) String previewId) {
        model.addAttribute("exhibitionAboutPage", aboutPageManager.getExhibitionAboutPage());
        model.addAttribute("aboutPageConfigured", true);
        model.addAttribute("languageCode", languageCode);
        
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        model.addAttribute("languages", exhibition.getLanguages());
        
        return "exhibition/aboutPagePublic";
    }

}
