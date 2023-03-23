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
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#storeAboutPageData()
     */
    @Override
    public void storeAboutPageData(AboutPageForm aboutPageForm) {


        ExhibitionAboutPage exhibitionAboutPage = getExhibitionAboutPage();       
        exhibitionAboutPage.setTitle(aboutPageForm.getTitle());
        exhibitionAboutPage.setAboutPageText(aboutPageForm.getAboutPageText());

        for(LocalizedTextForm title:aboutPageForm.getTitles())
        {        
            setAboutPageTitle(title,exhibitionAboutPage);
        }
        for(LocalizedTextForm aboutPageText:aboutPageForm.getAboutPageTexts())
        {
            setAboutPageDescription(aboutPageText,exhibitionAboutPage);
        }

    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager#store()
     */
    @Override
    public ExhibitionAboutPage store(ExhibitionAboutPage exhibitionAboutPage) {
        IExhibition exhibition = exhibitionManager.getStartExhibition();
        if(!exhibition.isAboutPageConfigured()) {
            exhibition.setAboutPageConfigured(true);
            exhibitionManager.storeExhibition((Exhibition)exhibition);
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
                    exhibitionLanguage.getLocalizedTexts().add(localizedText);
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
       
    /**
     * Creates About Page form object
     */
    @Override
    public AboutPageForm createAboutPageForm() {
        ExhibitionAboutPage exhibitionAboutPage = getExhibitionAboutPage(); 

        AboutPageForm aboutPageForm=new AboutPageForm();
        aboutPageForm.setAboutPageText(exhibitionAboutPage.getAboutPageText());
        aboutPageForm.setTitle(exhibitionAboutPage.getTitle());        
        
        IExhibition startExhibtion = exhibitionManager.getStartExhibition();
        
        IExhibitionLanguage defaultLanguage = startExhibtion.getLanguages().stream().filter(language -> language.isDefault()).findFirst().orElse(null);
        
        aboutPageForm.getTitles().add(createLocalizedTitleForm(exhibitionAboutPage, defaultLanguage));
        
        aboutPageForm.getAboutPageTexts().add(createLocalizedAboutTextForm(exhibitionAboutPage, defaultLanguage));
        
        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                aboutPageForm.getTitles().add(createLocalizedTitleForm(exhibitionAboutPage, language));
                
                aboutPageForm.getAboutPageTexts().add(createLocalizedAboutTextForm(exhibitionAboutPage, language)); 
            }


        });
        return aboutPageForm;
    }
    
    /**
     * Creates Localized title object for form 
     * 
     * @param exhibitionAboutPage
     * @param language
     * @return
     */
    @Override
    public LocalizedTextForm createLocalizedAboutTextForm(ExhibitionAboutPage exhibitionAboutPage,
            IExhibitionLanguage language) {

        LocalizedTextForm localizedAboutTextForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText aboutPageText = exhibitionAboutPage.getExhibitionTextDescriptions().stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(aboutPageText != null) {
            localizedAboutTextForm.setText(aboutPageText.getText());
            localizedAboutTextForm.setLocalisedTextId( aboutPageText.getId());

        } 
        localizedAboutTextForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedAboutTextForm;
    }
    
    /**
     * 
     * Creates Localized about text object for form 
     * @param exhibitionAboutPage
     * @param language
     * @return
     */
    @Override
    public LocalizedTextForm createLocalizedTitleForm(ExhibitionAboutPage exhibitionAboutPage, IExhibitionLanguage language) {
        LocalizedTextForm localizedTitleForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );

        ILocalizedText title = exhibitionAboutPage.getExhibitionTitles().stream()
                .filter(exhibitionTitle ->  StringUtils.equals(exhibitionTitle.getExhibitionLanguage().getId(), language.getId())).findAny().orElse(null);

        if(title != null) {
            localizedTitleForm.setText(title.getText());
            localizedTitleForm.setLocalisedTextId(title.getId());
        } 
        localizedTitleForm.setIsDefaultExhibitionLanguage(language.isDefault());      
        
        return localizedTitleForm;
    }
}
