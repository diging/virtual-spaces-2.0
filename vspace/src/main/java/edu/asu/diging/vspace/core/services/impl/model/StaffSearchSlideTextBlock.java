package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Slide;

public class StaffSearchSlideTextBlock {
    
    private List<Slide> slidesWithMatchedTextBlock;

    private Map<String, String> slideTextFirstImage;

    private Map<String, String> slideTextFirstTextBlock;

    public List<Slide> getSlidesWithMatchedTextBlock() {
        return slidesWithMatchedTextBlock;
    }

    public void setSlidesWithMatchedTextBlock(List<Slide> slidesWithMatchedTextBlock) {
        this.slidesWithMatchedTextBlock = slidesWithMatchedTextBlock;
    }

    public Map<String, String> getSlideTextFirstImage() {
        return slideTextFirstImage;
    }

    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage) {
        this.slideTextFirstImage = slideTextFirstImage;
    }

    public Map<String, String> getSlideTextFirstTextBlock() {
        return slideTextFirstTextBlock;
    }

    public void setSlideTextFirstTextBlock(Map<String, String> slideTextFirstTextBlock) {
        this.slideTextFirstTextBlock = slideTextFirstTextBlock;
    }
}