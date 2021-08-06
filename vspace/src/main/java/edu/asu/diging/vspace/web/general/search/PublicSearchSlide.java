package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Slide;

public class PublicSearchSlide {
    
    private List<Slide> slideList;
    
    private Map<String, String> slideFirstImage;

    public List<Slide> getSlideList() {
        return slideList;
    }

    public void setSlideList(List<Slide> slideList) {
        this.slideList = slideList;
    }

    public Map<String, String> getSlideFirstImage() {
        return slideFirstImage;
    }

    public void setSlideFirstImage(Map<String, String> slideFirstImage) {
        this.slideFirstImage = slideFirstImage;
    }

    
}
