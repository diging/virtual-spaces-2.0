package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;

public interface ISlideFactory {

    ISlide createSlide(String title, String description);

}
