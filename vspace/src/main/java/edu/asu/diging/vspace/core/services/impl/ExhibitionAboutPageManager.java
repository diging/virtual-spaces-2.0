package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
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
@Transactional
@Service
public class ExhibitionAboutPageManager implements IExhibitionAboutPageManager{

    @Autowired
    private ExhibitionAboutPageRepository repo;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;
    
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#findAll()
     */
    @Override
    public List<ExhibitionAboutPage> findAll() {
        Iterable<ExhibitionAboutPage> aboutpages = repo.findAll();
        List<ExhibitionAboutPage> results = new ArrayList<>();
        aboutpages.forEach(e -> results.add(e));
        return results;
    }
    
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#store()
     */
    @Override
    public ExhibitionAboutPage store(AboutPageForm aboutPageForm) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if(!exhibition.isAboutPageConfigured()) {
            exhibition.setAboutPageConfigured(true);
            exhibitionManager.storeExhibition((Exhibition)exhibition);
        }
        

        ExhibitionAboutPage exhibitionAboutPage = getExhibitionAboutPage();       
        exhibitionAboutPage.setTitle(aboutPageForm.getTitle());
        exhibitionAboutPage.setAboutPageText(aboutPageForm.getAboutPageText());

        for(LocalizedTextForm title:aboutPageForm.getTitles()) {        
            setAboutPageTitle(title,exhibitionAboutPage);
        }
        for(LocalizedTextForm aboutPageText:aboutPageForm.getAboutPageTexts()) {
            setAboutPageDescription(aboutPageText,exhibitionAboutPage);
        }
        
        return repo.save(exhibitionAboutPage);
    }
    
    /**
     * This method returns the first ExhibitionAboutPage set by a Staff, if the
     * ExhibitionAboutPage is not set yet, this method returns a new instance of 
     * an ExhibitionAbout Page
     * @return ExhibitionAboutPage
    */
    @Override
    public ExhibitionAboutPage getExhibitionAboutPage() {
        List<ExhibitionAboutPage> aboutPageList = findAll();
        return aboutPageList != null && !aboutPageList.isEmpty() ? aboutPageList.get(0):new ExhibitionAboutPage();
    }
    
    /**
     * This method maps the title in ExhibitionAboutPage, and add that to
     * exhibitionTitles list for each user selected Exhibition Language.
    */
    public void setAboutPageTitle(LocalizedTextForm title, ExhibitionAboutPage exhibitionAboutPage) {
        if(title!=null) {
            
            LocalizedText localizedText = localizedTextRepo.findById(title.getLocalisedTextId()).orElse(null);
            if(localizedText != null) {            
                localizedText.setText(title.getText());            
            } else {
                
                ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findById(title.getExhibitionLanguageId()).orElse(null);
                if(exhibitionLanguage != null) {
                    localizedText = new LocalizedText(exhibitionLanguage, title.getText());
                    exhibitionAboutPage.getExhibitionTitles().add(localizedText);
//                    exhibitionLanguage.getLocalizedTexts().add(localizedText);
                }
            }
            
        }

    }
    
    /**
     * This method maps the description in ExhibitionAboutPage, and add that to
     * exhibitionTextDescriptions list for each user selected Exhibition Language.
    */
    public void setAboutPageDescription(LocalizedTextForm aboutPageText, ExhibitionAboutPage exhibitionAboutPage) {
        if(aboutPageText!=null) {
            LocalizedText localizedText = localizedTextRepo.findById(aboutPageText.getLocalisedTextId()).orElse(null);
            
            if(localizedText != null) {
                localizedText.setText(aboutPageText.getText());
            } else {
                ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findById(aboutPageText.getExhibitionLanguageId()).orElse(null);
                if(exhibitionLanguage != null) {
                    exhibitionAboutPage.getExhibitionTextDescriptions().add(new LocalizedText(exhibitionLanguage, aboutPageText.getText()));
                }
            }        
        }
    } 
   
}
