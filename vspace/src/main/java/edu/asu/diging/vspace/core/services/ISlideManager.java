package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.display.SlideType;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideManager {

    ISlide getSlide(String slideId);

    ISlide createSlide(IModule module, SlideForm slideForm, SlideType type);

    void updateSlide(Slide slide);

    IBranchingPoint createBranchingPoint(IModule module, SlideForm slideForm, SlideType type);

    IChoice getChoice(String choiceId);

}
