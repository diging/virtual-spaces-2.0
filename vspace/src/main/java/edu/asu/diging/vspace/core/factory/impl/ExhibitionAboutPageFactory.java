package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.factory.IExhibitionAboutPageFactory;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionAboutPage;

@Component
public class ExhibitionAboutPageFactory implements IExhibitionAboutPageFactory{

    @Override
    public ExhibitionAboutPage createAboutPageForm(String title, String aboutPageText,
            List<ILocalizedText> localizedTitles, List<ILocalizedText> localizedDescriptions) {
        
        ExhibitionAboutPage exhibitionAboutPage = new ExhibitionAboutPage();
        
        exhibitionAboutPage.setTitle(title);
        exhibitionAboutPage.setAboutPageText(aboutPageText);
        exhibitionAboutPage.setExhibitionTitles(localizedTitles);
        exhibitionAboutPage.setExhibitionTextDescriptions(localizedDescriptions);
        
        return exhibitionAboutPage;
    }
    
    

}
