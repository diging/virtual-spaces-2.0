package edu.asu.diging.vspace.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.factory.IChoiceFactory;
import edu.asu.diging.vspace.core.factory.ISlideFactory;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.impl.SlideManager;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

@Service
public class SlideFactory implements ISlideFactory {
    
    @Autowired
    private IChoiceFactory choiceFactory;
    
    @Autowired
    private SlideManager slideManager;
    
    @Autowired
    private IExhibitionManager exhibitionManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ISlideFactory#createSlide(edu.asu.diging.vspace.core.model.IModule, edu.asu.diging.vspace.web.staff.forms.SlideForm)
     */
    @Override
    public ISlide createSlide(IModule module, SlideForm form, SlideType type) {
        
        ISlide slide;
        if(type.equals(SlideType.SLIDE)) {
            slide = new Slide();
        } else {
            slide = new BranchingPoint();            
            List<IChoice> choices = choiceFactory.createChoices(form.getChoices());
            ((IBranchingPoint) slide).setChoices(choices);
        }
        slide.setName(form.getName());
        slide.setDescription(form.getDescription());
        slide.setModule(module);
        slide.setContents(new ArrayList<IContentBlock>());
        return slide;        
    }
    
    /**
     * 
     * Creates new slide form object
     * @param slide
     * @return
     */
    @Override
    public SlideForm createNewSlideForm(ISlide slide) {
        SlideForm slideForm = new SlideForm();
        slideForm.setName(slide.getName());
        slideForm.setDescription(slide.getDescription());

        IExhibition startExhibtion = exhibitionManager.getStartExhibition();
        IExhibitionLanguage defaultLanguage = startExhibtion.getDefaultLanguage();

        slideForm.setDefaultName(createLocalizedTextForm(slide, defaultLanguage, slide.getSlideNames()));
        slideForm.setDefaultDescription(createLocalizedTextForm(slide, defaultLanguage, slide.getSlideDescriptions()));

        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                slideForm.getNames().add(createLocalizedTextForm(slide, language, slide.getSlideNames()));               
                slideForm.getDescriptions().add(createLocalizedTextForm(slide, language, slide.getSlideDescriptions())); 
            }
        });

        return slideForm;      
    }
    
    /**
     * Creates LocalizedTextForm form using provided list of localizedTexts. 
     * @param exhibitionAboutPage
     * @param language
     * @param localizedTexts
     * @return
     */
    private LocalizedTextForm createLocalizedTextForm(ISlide slide, IExhibitionLanguage language, List<ILocalizedText> localizedTexts) {

        LocalizedTextForm localizedSpaceForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText slideText = localizedTexts.stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(slideText != null) {
            localizedSpaceForm.setText(slideText.getText());
            localizedSpaceForm.setLocalisedTextId( slideText.getId());

        } 
        localizedSpaceForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedSpaceForm;
    }
    
    
}