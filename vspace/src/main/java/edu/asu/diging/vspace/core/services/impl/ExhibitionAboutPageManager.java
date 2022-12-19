package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.data.ExhibitionAboutPageRepository;
import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.LanguageDescriptionObjectRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
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
    private LanguageDescriptionObjectRepository languageObjectRepo;
    
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
			
        for(LanguageDescriptionObject titles:languageAboutPage.getTitles())
        {
            setAboutPageTitle(titles.getUserText(),exhibitionAboutPage);
        }
        for(LanguageDescriptionObject texts:languageAboutPage.getAboutPageTexts())
        {
            setAboutPageDescription(texts.getUserText(),exhibitionAboutPage);
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
    public void setAboutPageTitle(String title, ExhibitionAboutPage exhibitionAboutPage) {
        if(title!=null || title.length()!=0)
	{
            LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
            languageObject.setUserText(title);
            if(exhibitionAboutPage.getExhibitionTitles() == null) {
                exhibitionAboutPage.setExhibitionTitles(new ArrayList());
            }
            exhibitionAboutPage.getExhibitionTitles().add(languageObject);
            storeLanguageObject(languageObject);
        }
        
    }
    /**
     * This method maps the description in ExhibitionAboutPage, and add that to
     * exhibitionTextDescriptions list for each user selected Exhibition Language.
    */
    public void setAboutPageDescription(String aboutPageTexts, ExhibitionAboutPage exhibitionAboutPage) {
        if(aboutPageTexts!=null || aboutPageTexts.length()!=0)
	{
            LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
            languageObject.setUserText(aboutPageTexts);
            if(exhibitionAboutPage.getExhibitionTextDescriptions() == null) {
                exhibitionAboutPage.setExhibitionTextDescriptions(new ArrayList());
            }
            exhibitionAboutPage.getExhibitionTextDescriptions().add(languageObject);
            storeLanguageObject(languageObject);
	}
      
    }
	/**
     * This method save the LanguageDescriptionObject in database.
    */
    private void storeLanguageObject(LanguageDescriptionObject languageObject) {
        languageObjectRepo.save(languageObject);
    }   
}
