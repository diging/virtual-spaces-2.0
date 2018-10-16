package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface ISequence extends IVSpaceElement {
	
	List<ISlide> getSlides();
	
	void setSlides(List<ISlide> slides);
	
}
