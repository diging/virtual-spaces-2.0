package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.web.staff.forms.LocalizedTextForm;

import java.util.List;

import edu.asu.diging.vspace.core.model.ILocalizedText;

public interface ILocalizedTextFormDataManager {
    
    public void addLocalizedDetails(Object entity, LocalizedTextForm localizedTextFormData, List<ILocalizedText> detailList);

}
