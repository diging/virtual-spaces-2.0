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
     * Creates new slide form object
     * @param slide
     * @return
     */
    @Override
    public SlideForm createNewSlideForm(ISlide slide, IExhibition startExhibition) {
        SlideForm slideForm = new SlideForm();
        slideForm.setName(slide.getName());
        slideForm.setDescription(slide.getDescription());
        IExhibitionLanguage defaultLanguage = exhibitionManager.getDefaultLanguage(startExhibition);
        slideForm.setDefaultName(localizedTextFormCreation.createLocalizedTextForm(defaultLanguage, slide.getSlideNames()));
        slideForm.setDefaultDescription(localizedTextFormCreation.createLocalizedTextForm(defaultLanguage, slide.getSlideDescriptions()));
        startExhibition.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                slideForm.getNames().add(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideNames()));               
                slideForm.getDescriptions().add(localizedTextFormCreation.createLocalizedTextForm(language, slide.getSlideDescriptions())); 
            }
        });
        return slideForm;
    }
}