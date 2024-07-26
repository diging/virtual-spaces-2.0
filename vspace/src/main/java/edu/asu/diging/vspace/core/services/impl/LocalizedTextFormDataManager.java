package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.ILocalizedTextFormDataManager;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

@Service
public class LocalizedTextFormDataManager implements ILocalizedTextFormDataManager{
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;

    /**
     * Adds localized details (names or descriptions) to the specified list.
     * 
     * @param entity The entity (slide or space) to which the details will be added.
     * @param localizedTextFormData The localized text form containing the details to be added.
     * @param detailList The list in the entity where the details will be added (e.g., slideNames, spaceNames).
     */
    @Override
    public void addLocalizedDetails(Object entity, LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList) {
        if (entity == null || StringUtils.isEmpty(localizedTextFormData.getText())) {
            return;
        }
        
        LocalizedText localizedText = localizedTextRepo.findById(localizedTextFormData.getLocalizedTextId()).orElse(null);
        if (localizedText != null) {
            localizedText.setText(localizedTextFormData.getText());
        } else {
            ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findById(localizedTextFormData.getExhibitionLanguageId()).orElse(null);
            if (exhibitionLanguage != null) {
                localizedText = new LocalizedText(exhibitionLanguage, localizedTextFormData.getText());
                detailList.add(localizedText);
            }
        }
    }


}
