package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.ILocalizedText;
import edu.asu.diging.vspace.core.model.impl.LocalizedText;
import edu.asu.diging.vspace.core.services.ILocalizedTextFormDataManager;
import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

public interface ILocalizedTextFactory {

    ILocalizedText createLocalizedText(Object entity, LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList);

}