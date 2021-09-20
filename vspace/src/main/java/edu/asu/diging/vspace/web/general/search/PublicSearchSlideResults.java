package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.ISlide;

public class PublicSearchSlideResults {
    
    private List<ISlide> slides;

    private Map<String, String> firstImageOfSlide;

    public List<ISlide> getSlides() {
        return slides;
    }

    public void setSlides(List<ISlide> slides) {
        this.slides = slides;
    }

    public Map<String, String> getFirstImageOfSlide() {
        return firstImageOfSlide;
    }

    public void setFirstImageOfSlide(Map<String, String> firstImageOfSlide) {
        this.firstImageOfSlide = firstImageOfSlide;
    }

    
}
