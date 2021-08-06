package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearch {
    
    private List<Space> spaceList;

    private List<Module> moduleList;
    
    private  Map<String,String> moduleFirstSlideFirstImage;

    private List<Slide> slideList;
    
    private Map<String, String> slideFirstImage;

    private List<Slide> slideTextList;
    
    private Map<String, String> slideTextFirstImage;

    public List<Space> getSpaceList() {
        return spaceList;
    }

    public void setSpaceList(List<Space> spaceList) {
        this.spaceList = spaceList;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public Map<String, String> getModuleFirstSlideFirstImage() {
        return moduleFirstSlideFirstImage;
    }

    public void setModuleFirstSlideFirstImage(Map<String, String> moduleFirstSlideFirstImage) {
        this.moduleFirstSlideFirstImage = moduleFirstSlideFirstImage;
    }

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
