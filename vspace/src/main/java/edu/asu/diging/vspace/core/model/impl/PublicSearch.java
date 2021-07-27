package edu.asu.diging.vspace.core.model.impl;

import java.util.HashSet;
import java.util.Map;

import edu.asu.diging.vspace.core.model.ISearch;

public class PublicSearch implements ISearch {
    
    private HashSet<Space> spaceSet;

    private HashSet<Module> moduleSet;
    
    private  Map<String,String> moduleFirstSlideFirstImage;

    private HashSet<Slide> slideSet;
    
    private Map<String, String> slideFirstImage;

    private HashSet<Slide> slideTextSet;
    
    private Map<String, String> slideTextFirstImage;

    @Override
    public HashSet<Space> getSpaceSet() {
        return spaceSet;
    }

    @Override
    public void setSpaceSet(HashSet<Space> spaceSet) {
        this.spaceSet = spaceSet;
    }

    @Override
    public HashSet<Module> getModuleSet() {
        return moduleSet;
    }

    @Override
    public void setModuleSet(HashSet<Module> moduleSet) {
        this.moduleSet = moduleSet;
    }

    @Override
    public Map<String, String> getModuleFirstSlideFirstImage() {
        return moduleFirstSlideFirstImage;
    }

    @Override
    public void setModuleFirstSlideFirstImage(Map<String, String> moduleFirstSlideFirstImage) {
        this.moduleFirstSlideFirstImage = moduleFirstSlideFirstImage;
    }

    @Override
    public HashSet<Slide> getSlideSet() {
        return slideSet;
    }

    @Override
    public void setSlideSet(HashSet<Slide> slideSet) {
        this.slideSet = slideSet;
    }

    @Override
    public Map<String, String> getSlideFirstImage() {
        return slideFirstImage;
    }

    @Override
    public void setSlideFirstImage(Map<String, String> slideFirstImage) {
        this.slideFirstImage = slideFirstImage;
    }

    @Override
    public HashSet<Slide> getSlideTextSet() {
        return slideTextSet;
    }

    @Override
    public void setSlideTextSet(HashSet<Slide> slideTextSet) {
        this.slideTextSet = slideTextSet;
    }

    @Override
    public Map<String, String> getSlideTextFirstImage() {
        return slideTextFirstImage;
    }

    @Override
    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage) {
        this.slideTextFirstImage = slideTextFirstImage;
    }

}
