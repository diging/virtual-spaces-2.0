package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideFactory {

    //ISlide createBranchingPoint(IModule module, SlideForm form);

    ISlide createSlide(String type, IModule module, SlideForm form);

}
