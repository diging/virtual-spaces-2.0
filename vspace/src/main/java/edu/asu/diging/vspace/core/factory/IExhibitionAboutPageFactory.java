package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;
import edu.asu.diging.vspace.web.staff.forms.AboutPageForm;

public interface IExhibitionAboutPageFactory {

    /**
     * Creates About Page form object
     */
    AboutPageForm createAboutPageForm(ExhibitionAboutPage exhibitionAboutPage);

}
