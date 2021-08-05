package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.ISearch;

public class StaffSearch implements ISearch {
    
    private List<Space> spaceList;

    private List<Module> moduleList;
    
    private  Map<String,String> moduleFirstSlideFirstImage;

    private List<Slide> slideList;
    
    private Map<String, String> slideFirstImage;

    private List<Slide> slideTextList;
    
    private Map<String, String> slideTextFirstImage;

    @Override
    public List<Space> getSpaceList() {
        return spaceList;
    }

    @Override
    public void setSpaceList(List<Space> spaceList) {
        this.spaceList = spaceList;
    }

    @Override
    public List<Module> getModuleList() {
        return moduleList;
    }

    @Override
    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
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
    public List<Slide> getSlideList() {
        return slideList;
    }

    @Override
    public void setSlideList(List<Slide> slideList) {
        this.slideList = slideList;
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
    public List<Slide> getSlideTextList() {
        return slideTextList;
    }

    @Override
    public void setSlideTextList(List<Slide> slideTextList) {
        this.slideTextList = slideTextList;
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
