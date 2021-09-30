package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.ISlide;

public class PublicSearchSlideTextBlockResults {
    
    private List<ISlide> slidesWithMatchedTextBlock;

    private Map<String, String> slideToFirstImageMap;

    private Map<String, String> slideToFirstTextBlockMap;

    public List<ISlide> getSlidesWithMatchedTextBlock() {
        return slidesWithMatchedTextBlock;
    }

    public void setSlidesWithMatchedTextBlock(List<ISlide> slidesWithMatchedTextBlock) {
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