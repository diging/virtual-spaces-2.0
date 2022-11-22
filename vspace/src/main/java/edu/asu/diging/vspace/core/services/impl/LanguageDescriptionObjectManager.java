package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.LanguageDescriptionObjectRepository;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.core.services.ILanguageDescriptionObjectManager;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;

@Transactional
@Service
public class LanguageDescriptionObjectManager implements ILanguageDescriptionObjectManager {
	
	 @Autowired
	 private LanguageDescriptionObjectRepository repo;

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
		return exhibitionAboutPage;				
		
	}
	
	public void setAboutPageTitle(String title, ExhibitionAboutPage exhibitionAboutPage) {
    	LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
        languageObject.setUserText(title);
        //languageObject.setExhibitionLanguage(null);
        if(exhibitionAboutPage.getExhibitionTitles() == null) {
        	exhibitionAboutPage.setExhibitionTitles(new ArrayList());
        }
        exhibitionAboutPage.getExhibitionTitles().add(languageObject);
        storeLanguageObject(languageObject);
    }

	public void setAboutPageDescription(String aboutPageTexts, ExhibitionAboutPage exhibitionAboutPage) {             
	  LanguageDescriptionObject languageObject = new LanguageDescriptionObject();
      languageObject.setUserText(aboutPageTexts);    
      //languageObject.setExhibitionLanguage(null);  
      if(exhibitionAboutPage.getExhibitionTextDescriptions() == null) {
    	  exhibitionAboutPage.setExhibitionTextDescriptions(new ArrayList());
      }
      exhibitionAboutPage.getExhibitionTextDescriptions().add(languageObject);
      storeLanguageObject(languageObject);
	}
	
	private void storeLanguageObject(LanguageDescriptionObject languageObject) {
		repo.save(languageObject);
		
	}

	
		
}
