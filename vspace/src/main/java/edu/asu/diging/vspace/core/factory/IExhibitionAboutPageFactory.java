package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;

public interface IExhibitionAboutPageFactory {
    
    /**
     * Creates Exhibition About Page
     */
    ExhibitionAboutPage createExhibitionAboutPage(String title, String aboutPageText,  List<ILocalizedText> localizedTitles, List<ILocalizedText> localizedDescriptions);

}
