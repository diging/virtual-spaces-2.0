package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideManager {

    ISlide getSlide(String slideId);

    ISlide createSlide(String moduleId, SlideForm slideForm);

    void updateSlide(Slide slide);

    boolean deleteSlideById(String slideId) throws SlideDoesNotExistException;

}
