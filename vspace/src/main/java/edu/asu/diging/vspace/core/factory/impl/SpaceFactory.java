package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFactory implements ISpaceFactory {
    
    @Autowired
    private SpaceRepository spaceRepo;
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;
    

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
	
}
