package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideFactory {

    ISlide createSlide(SlideForm form);

}
