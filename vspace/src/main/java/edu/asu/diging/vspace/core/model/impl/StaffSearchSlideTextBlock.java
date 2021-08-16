package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchSlideTextBlock {
    
    private List<Slide> slidesWithMatchedTextBlock;

    private Map<String, String> slideToFirstImageMap;

    private Map<String, String> slideToFirstTextBlockMap;

    public List<Slide> getSlidesWithMatchedTextBlock() {
        return slidesWithMatchedTextBlock;
    }

    public void setSlidesWithMatchedTextBlock(List<Slide> slidesWithMatchedTextBlock) {
        this.slidesWithMatchedTextBlock = slidesWithMatchedTextBlock;
    }

    public Map<String, String> getSlideToFirstImageMap() {
        return slideToFirstImageMap;
    }

    public void setSlideToFirstImageMap(Map<String, String> slideToFirstImageMap) {
        this.slideToFirstImageMap = slideToFirstImageMap;
    }

    public Map<String, String> getSlideToFirstTextBlockMap() {
        return slideToFirstTextBlockMap;
    }

    public void setSlideToFirstTextBlockMap(Map<String, String> slideToFirstTextBlockMap) {
        this.slideToFirstTextBlockMap = slideToFirstTextBlockMap;
    }
}
