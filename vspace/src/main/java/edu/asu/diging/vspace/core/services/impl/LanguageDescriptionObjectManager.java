package edu.asu.diging.vspace.core.services.impl;

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
	 private ExhibitionRepository exhibitRepo;

	@Override
	public ExhibitionAboutPage storeAboutPageData(ExhibitionAboutPage exhibitionAboutPage,AboutPageForm languageAboutPage) {
		// TODO Auto-generated method stub
	
		for(LanguageDescriptionObject titles:languageAboutPage.getTitles())
		{
			exhibitionAboutPage.setAboutPageTitle(titles.getUserText());
		}
		for(LanguageDescriptionObject texts:languageAboutPage.getAboutPageTexts())
		{
			exhibitionAboutPage.setAboutPageDescription(texts.getUserText());
		}
		
		return exhibitionAboutPage;
		//System.out.println(exhibitionAboutPage.getExhibitionTitles());

		
		
	}
		
}
