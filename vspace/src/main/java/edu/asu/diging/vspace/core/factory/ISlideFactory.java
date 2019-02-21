package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISlide;

public interface ISlideFactory {

    ISlide createSlide(IModule module, String title, String description);

}
