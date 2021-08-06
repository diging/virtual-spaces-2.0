package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Slide;

public class PublicSearchSlideText {
    
    private List<Slide> slideTextList;
    
    private Map<String, String> slideTextFirstImage;

    public List<Slide> getSlideTextList() {
        return slideTextList;
    }

    public void setSlideTextList(List<Slide> slideTextList) {
        this.slideTextList = slideTextList;
    }

    public Map<String, String> getSlideTextFirstImage() {
        return slideTextFirstImage;
    }

    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage) {
        this.slideTextFirstImage = slideTextFirstImage;
    }
    
}