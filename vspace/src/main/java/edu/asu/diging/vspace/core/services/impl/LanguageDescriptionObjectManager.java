package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.asu.diging.vspace.core.data.LanguageDescriptionObjectRepository;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;

@Transactional
@Service
public class LanguageDescriptionObjectManager implements ILanguageDescriptionObject {
	@Autowired
    private LanguageDescriptionObjectRepository repo;
	
	@Autowired
	private ExhibitionAboutPage exhibitionAboutPage;

	@Override
	public AboutPageForm store(AboutPageForm aboutPageLanguage) {
		// TODO Auto-generated method stub
		List<LanguageDescriptionObject> titles=aboutPageLanguage.getTitles();
		List<LanguageDescriptionObject> texts=aboutPageLanguage.getAboutPageTexts();
		//exhibitionAboutPage.setExhibitionTitles();
		aboutPageLanguage.getTitles();
		return null;
	}

}
