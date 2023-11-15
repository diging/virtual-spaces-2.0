package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.display.ISlideDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;

public interface ISlideDisplayManager {
    ISlideDisplay getBySlide(ISlide slide);

}
