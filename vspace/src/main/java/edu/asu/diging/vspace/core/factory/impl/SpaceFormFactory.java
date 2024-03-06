package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceFormFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.services.IExhibitionManager;
import edu.asu.diging.vspace.core.services.ISpaceManager;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFormFactory implements ISpaceFormFactory {
    
    @Autowired
    private IExhibitionManager exhibitionManager;
    
    @Autowired
    private ISpaceManager spaceManager;
    
    @Autowired
    private LocalizedTextFormCreation localizedTextFormCreation;

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
        
        spaceForm.setDefaultName(localizedTextFormCreation.createLocalizedTextForm(startExhibtion.getDefaultLanguage(), space.getSpaceNames()));
        spaceForm.setDefaultDescription(localizedTextFormCreation.createLocalizedTextForm(startExhibtion.getDefaultLanguage(), space.getSpaceDescriptions()));
        
        startExhibtion.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                spaceForm.getNames().add(localizedTextFormCreation.createLocalizedTextForm(language, space.getSpaceNames()));               
                spaceForm.getDescriptions().add(localizedTextFormCreation.createLocalizedTextForm(language, space.getSpaceDescriptions())); 
            }
        });
        
        return spaceForm;      
    }
    
    @Override
    public SpaceForm getSpaceForm(String spaceId) {
        ISpace space = spaceManager.getSpace(spaceId);
        SpaceForm slideForm = createNewSpaceForm(space);   
        slideForm.setName(space.getName());
        slideForm.setDescription(space.getDescription());
        return slideForm; 

    }
}