package edu.asu.diging.vspace.core.model.impl;

import java.util.HashSet;
import java.util.Map;

public class StaffSearch {
    
    private HashSet<Space> spaceSet;

    private HashSet<Module> moduleSet;
    
    private  Map<String,String> moduleFirstSlideFirstImage;

    private HashSet<Slide> slideSet;
    
    private Map<String, String> slideFirstImage;

    private HashSet<Slide> slideTextSet;
    
    private Map<String, String> slideTextFirstImage;

    public HashSet<Space> getSpaceSet() {
        return spaceSet;
    }

    public void setSpaceSet(HashSet<Space> spaceSet) {
        this.spaceSet = spaceSet;
    }

    public HashSet<Module> getModuleSet() {
        return moduleSet;
    }

    public void setModuleSet(HashSet<Module> moduleSet) {
        this.moduleSet = moduleSet;
    }

    public Map<String, String> getModuleFirstSlideFirstImage() {
        return moduleFirstSlideFirstImage;
    }

    public void setModuleFirstSlideFirstImage(Map<String, String> moduleFirstSlideFirstImage) {
        this.moduleFirstSlideFirstImage = moduleFirstSlideFirstImage;
    }

    public HashSet<Slide> getSlideSet() {
        return slideSet;
    }

    public void setSlideSet(HashSet<Slide> slideSet) {
        this.slideSet = slideSet;
    }

    public Map<String, String> getSlideFirstImage() {
        return slideFirstImage;
    }

    public void setSlideFirstImage(Map<String, String> slideFirstImage) {
        this.slideFirstImage = slideFirstImage;
    }

    public HashSet<Slide> getSlideTextSet() {
        return slideTextSet;
    }

    public void setSlideTextSet(HashSet<Slide> slideTextSet) {
        this.slideTextSet = slideTextSet;
    }

    public Map<String, String> getSlideTextFirstImage() {
        return slideTextFirstImage;
    }

    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage) {
        this.slideTextFirstImage = slideTextFirstImage;
    }

}
