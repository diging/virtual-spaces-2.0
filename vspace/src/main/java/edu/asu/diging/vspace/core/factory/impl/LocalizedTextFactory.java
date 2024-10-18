package edu.asu.diging.vspace.core.factory.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import edu.asu.diging.vspace.core.data.ExhibitionLanguageRepository;
import edu.asu.diging.vspace.core.data.LocalizedTextRepository;
import edu.asu.diging.vspace.core.factory.ILocalizedTextFactory;
import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.ExhibitionLanguage;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

@Service
@Transactional
public class LocalizedTextFactory implements ILocalizedTextFactory {
    
    @Autowired
    private LocalizedTextRepository localizedTextRepo;
    
    @Autowired
    private ExhibitionLanguageRepository exhibitionLanguageRepository;
    
    /**
     * Adds localized text (names or descriptions) to the specified list.
     * 
     * @param entity The entity (slide or space) to which the details will be added.
     * @param localizedTextFormData The localized text form containing the details to be added.
     * @param detailList The list in the entity where the details will be added (e.g., slideNames, spaceNames).
     */
    @Override
    public LocalizedText createLocalizedText(Object entity, LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList) {
        if (StringUtils.isEmpty(localizedTextFormData.getText())) {
            return null;
        }
        LocalizedText localizedText = localizedTextRepo.findById(localizedTextFormData.getLocalizedTextId()).orElse(null);
        if (localizedText != null) {
            localizedText.setText(localizedTextFormData.getText());
        } else {
            ExhibitionLanguage exhibitionLanguage = exhibitionLanguageRepository.findById(localizedTextFormData.getExhibitionLanguageId()).orElse(null);
            if (exhibitionLanguage != null) {
                localizedText = new LocalizedText(exhibitionLanguage, localizedTextFormData.getText());
            }
            detailList.add(localizedText);
        }
        
        return localizedText;
    }
}
