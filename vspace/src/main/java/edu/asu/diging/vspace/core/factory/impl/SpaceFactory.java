package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IExhibitionLanguage;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFactory implements ISpaceFactory {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private SpaceRepository spaceRepo;
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;
    
    /**
     * 
     * Creates new space form object
     * @param space
     * @return
     */
    @Override
    public SpaceForm createNewSpaceForm(ISpace space) {
        SpaceForm spaceForm = new SpaceForm();
        spaceForm.setName(space.getName());
        spaceForm.setDescription(space.getDescription());
        
        IExhibition startExhibtion = exhibitionManager.getStartExhibition();
        IExhibitionLanguage defaultLanguage = startExhibtion.getDefaultLanguage();
        
        spaceForm.setDefaultName(createLocalizedTextForm(space, defaultLanguage, space.getSpaceNames()));
        spaceForm.setDefaultDescription(createLocalizedTextForm(space, defaultLanguage, space.getSpaceDescriptions()));
        
        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                spaceForm.getNames().add(createLocalizedTextForm(space, language, space.getSpaceNames()));               
                spaceForm.getDescriptions().add(createLocalizedTextForm(space, language, space.getSpaceDescriptions())); 
            }
        });
        
        return spaceForm;      
    }
    

    @Override
    public ISpace createSpace(SpaceForm form) {
        ISpace space = new Space();
        space.setName(form.getName());
        space.setDescription(form.getDescription());
        return spaceRepo.save((Space) space);
    }
    
    /**
     * Adds description to spaceDescription list.
     * @param space
     * @param descriptions
     */
    @Override
    public void addSpaceDescription(ISpace space, LocalizedTextForm description) {
        if(description!=null && !StringUtils.isEmpty(description.getText())) {
            LocalizedText localizedText = localizedTextRepo.findById(description.getLocalisedTextId()).orElse(null);
            if(localizedText != null) {
                localizedText.setText(description.getText());
            } else {
                ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findById(description.getExhibitionLanguageId()).orElse(null);
                if(exhibitionLanguage != null) {
                    LocalizedText newLocalizedText = new LocalizedText(exhibitionLanguage, description.getText());
                    space.getSpaceDescriptions().add(newLocalizedText);
                }
            }
        }
    }
    
    /**
     * Creates LocalizedTextForm form using provided list of localizedTexts. 
     * @param exhibitionAboutPage
     * @param language
     * @param localizedTexts
     * @return
     */
    private LocalizedTextForm createLocalizedTextForm(ISpace space, IExhibitionLanguage language, List<ILocalizedText> localizedTexts) {

        LocalizedTextForm localizedSpaceForm = new LocalizedTextForm(null, null,  language.getId(), language.getLabel() );
        ILocalizedText spaceText = localizedTexts.stream()
                .filter(exhibitionText -> StringUtils.equals(language.getId(), exhibitionText.getExhibitionLanguage().getId())).findAny().orElse(null);

        if(spaceText != null) {
            localizedSpaceForm.setText(spaceText.getText());
            localizedSpaceForm.setLocalisedTextId( spaceText.getId());

        } 
        localizedSpaceForm.setIsDefaultExhibitionLanguage(language.isDefault());
        return localizedSpaceForm;
    }

    

	
}
