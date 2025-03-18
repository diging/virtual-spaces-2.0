package edu.asu.diging.vspace.web.staff.forms.factory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISlideFormFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideFormFactory implements ISlideFormFactory{
    
    @Autowired
    private LocalizedTextFormFactory localizedTextFormCreation;
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    /**
     * 
     * Creates new slide form object based on the slide and the exhibition
     * 
     * This method initializes a {@code SlideForm} object and populates it with the name and 
     * description of the given slide. It then processes the exhibition's supported languages, setting 
     * the default name and description for the default language, and adds localized names and 
     * descriptions for other languages.
     * 
     * @param slide The {@code ISlide} containing the slide details.
     * @param startExhibition The {@code IExhibition} from which the language settings are derived.
     * @return A {@code SlideForm} containing localized slide information.
     */
    @Override
    public SlideForm createNewSlideForm(ISlide slide, IExhibition startExhibition) {
        SlideForm slideForm = new SlideForm();
        slideForm.setName(slide.getName());
        slideForm.setDescription(slide.getDescription());
        IExhibitionLanguage defaultLanguage = exhibitionManager.getDefaultLanguage(startExhibition);
        
        startExhibition.getLanguages().forEach(language -> {
            if(language.isDefault()) {
                slideForm.setDefaultName(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideNames()));               
                slideForm.setDefaultDescription(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideDescriptions())); 
            }
            else {
                slideForm.getNames().add(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideNames()));
                slideForm.getDescriptions().add(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideDescriptions())); 
            }
        });
        return slideForm;
    }
}