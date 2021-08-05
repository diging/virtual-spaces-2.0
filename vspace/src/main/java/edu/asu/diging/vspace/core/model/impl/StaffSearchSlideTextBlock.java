package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchSlideTextBlock {

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

    public Map<String, String> getSlideTextFirstTextBlock() {
        return slideTextFirstTextBlock;
    }

    public void setSlideTextFirstTextBlock(Map<String, String> slideTextFirstTextBlock) {
        this.slideTextFirstTextBlock = slideTextFirstTextBlock;
    }

    private List<Slide> slideTextList;

    private Map<String, String> slideTextFirstImage;

    private Map<String, String> slideTextFirstTextBlock;

}
