package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;
import java.util.Map;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class StaffSearchSlide {
    
    private List<Slide> slide;

    private Map<String, String> slideFirstImage;

    public List<Slide> getSlide() {
        return slide;
    }

    public void setSlide(List<Slide> slide) {
        this.slide = slide;
    }

    public Map<String, String> getSlideFirstImage() {
        return slideFirstImage;
    }

    public void setSlideFirstImage(Map<String, String> slideFirstImage) {
        this.slideFirstImage = slideFirstImage;
    }
}
