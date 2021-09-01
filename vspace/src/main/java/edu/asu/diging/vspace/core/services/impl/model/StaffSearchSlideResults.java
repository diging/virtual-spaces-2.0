package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Slide;

public class StaffSearchSlideResults {
    
    private List<Slide> slides;

    private Map<String, String> firstImageOfSlide;

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

    public Map<String, String> getFirstImageOfSlide() {
        return firstImageOfSlide;
    }

    public void setFirstImageOfSlide(Map<String, String> firstImageOfSlide) {
        this.firstImageOfSlide = firstImageOfSlide;
    }
}
