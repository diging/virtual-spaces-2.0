package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.exception.SlideDoesNotExistException;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideManager {

    ISlide getSlide(String slideId);

    ISlide createSlide(String moduleId, SlideForm slideForm);

    void updateSlide(Slide slide);

    void deleteSlideById(String slideId, String moduleId) throws SlideDoesNotExistException;

    List<Sequence> getSlideSequences(String slideId, String moduleId);

}
