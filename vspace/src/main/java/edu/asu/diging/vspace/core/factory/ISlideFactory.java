package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.web.staff.forms.SlideForm;

public interface ISlideFactory {

    ISlide createSlide(IModule modfule, SlideForm form);

    ISlide createBranchingPoint(IModule module, SlideForm form, List<IChoice> choices);

}
