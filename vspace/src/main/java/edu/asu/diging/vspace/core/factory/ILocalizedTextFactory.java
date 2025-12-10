package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

public interface ILocalizedTextFactory {

//    ILocalizedText createLocalizedText(Object entity, LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList);

    /**
     * Adds localized text (names or descriptions) to the specified list.
     * 
     * @param entity The entity (slide or space) to which the details will be added.
     * @param localizedTextFormData The localized text form containing the details to be added.
     * @param detailList The list in the entity where the details will be added (e.g., slideNames, spaceNames).
     */
    LocalizedText createLocalizedText(IVSpaceElement entity, LocalizedText localizedText,
            LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList);

    LocalizedText createOrUpdateLocalizedText(IVSpaceElement entity, LocalizedTextForm localizedTextFormData,
            List<ILocalizedText> detailList);

    LocalizedText updateLocalizedText(LocalizedText localizedText, LocalizedTextForm localizedTextFormData);

}