package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ILanguageDescriptionObject;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.core.model.impl.LanguageDescriptionObject;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;

public interface ILanguageDescriptionObjectManager {
	ExhibitionAboutPage storeAboutPageData(ExhibitionAboutPage exhibitionAboutPage,AboutPageForm languageAboutPage);

	//LanguageDescriptionObject store(ExhibitionAboutPage exhibitionAboutPage);

}
