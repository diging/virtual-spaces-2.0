package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideManager {

    ISlide storeSlide(ISlide slide);

    ISlide getSlide(String slideId);

    ISlide createSlide(String moduleId, SlideForm slideForm);

}
