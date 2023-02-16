package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.LanguageDescriptionObjectRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.IExhibitionAboutPageManager;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;
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
    public ExhibitionAboutPage storeAboutPageData(ExhibitionAboutPage exhibitionAboutPage,AboutPageForm languageAboutPage) {
			
        for(ILocalizedText title:languageAboutPage.getTitles())
        {
            setAboutPageTitle(title,exhibitionAboutPage);
        }
        for(ILocalizedText aboutPageText:languageAboutPage.getAboutPageTexts())
        {
            setAboutPageDescription(aboutPageText,exhibitionAboutPage);
        }
        store(exhibitionAboutPage);
        return exhibitionAboutPage;				
		
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
    public void setAboutPageTitle(ILocalizedText title, ExhibitionAboutPage exhibitionAboutPage) {
        if(title!=null) {

            ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findByLabel(title.getExhibitionLanguage().getLabel());
            if(exhibitionLanguage != null) {
                title.setExhibitionLanguage(exhibitionLanguage);
            }
            Optional<ILocalizedText> text = exhibitionAboutPage.getExhibitionTitles().stream().filter(exhibitionTitle-> {
                return  exhibitionTitle.getExhibitionLanguage().equals(exhibitionLanguage);
            }).findAny();
            if(text.isPresent()) {
                text.get().setText(title.getText());
            } else {
                exhibitionAboutPage.getExhibitionTitles().add(title);
            }
        }

    }
    
    /**
     * This method maps the description in ExhibitionAboutPage, and add that to
     * exhibitionTextDescriptions list for each user selected Exhibition Language.
    */
    public void setAboutPageDescription(ILocalizedText aboutPageText, ExhibitionAboutPage exhibitionAboutPage) {
        if(aboutPageText!=null) {

            ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findByLabel(aboutPageText.getExhibitionLanguage().getLabel());
            if(exhibitionLanguage != null) {
                aboutPageText.setExhibitionLanguage(exhibitionLanguage);
            }
            Optional<ILocalizedText> text =   exhibitionAboutPage.getExhibitionTextDescriptions().stream().filter(textDescription -> {
                return  textDescription.getExhibitionLanguage().equals(exhibitionLanguage);
            }).findAny();

            if(text.isPresent()) {
                text.get().setText(aboutPageText.getText());
            } else {
                exhibitionAboutPage.getExhibitionTextDescriptions().add(aboutPageText);
            }
        }

    }
}
