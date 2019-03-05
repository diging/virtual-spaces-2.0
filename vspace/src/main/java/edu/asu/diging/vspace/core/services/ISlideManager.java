package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.ISlide;

public interface ISlideManager {

    ISlide storeSlide(ISlide slide);

    ISlide getSlide(String slideId);

}
