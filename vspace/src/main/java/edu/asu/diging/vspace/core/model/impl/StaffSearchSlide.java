package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchSlide {
    
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
