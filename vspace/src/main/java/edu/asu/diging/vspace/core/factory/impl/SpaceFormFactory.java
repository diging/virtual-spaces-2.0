package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceFormFactory;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFormFactory implements ISpaceFormFactory {
    
    @Autowired
    private LocalizedTextFormFactory localizedTextFormCreation;

    /**
     * 
     * Creates new space form object
     * @param space
     * @return
     */
    @Override
    public SpaceForm createNewSpaceForm(ISpace space, IExhibition startExhibition) {
        SpaceForm spaceForm = new SpaceForm();
        spaceForm.setName(space.getName());
        spaceForm.setDescription(space.getDescription());
        
        startExhibition.getLanguages().forEach(language -> {
            if(!language.isDefault()) {
                spaceForm.getNames().add(localizedTextFormCreation.createLocalizedTextForm(language, space.getSpaceNames()));               
                spaceForm.getDescriptions().add(localizedTextFormCreation.createLocalizedTextForm(language, space.getSpaceDescriptions())); 
            } else {
                spaceForm.setDefaultName(localizedTextFormCreation.createLocalizedTextForm(startExhibition.getDefaultLanguage(), space.getSpaceNames()));
                spaceForm.setDefaultDescription(localizedTextFormCreation.createLocalizedTextForm(startExhibition.getDefaultLanguage(), space.getSpaceDescriptions()));
            }
        });
        
        return spaceForm;      
    }
    
    @Override
    public SpaceForm getSpaceForm(ISpace space, IExhibition startExhibition) {
        SpaceForm slideForm = createNewSpaceForm(space, startExhibition);   
        slideForm.setName(space.getName());
        slideForm.setDescription(space.getDescription());
        return slideForm; 
    }
}