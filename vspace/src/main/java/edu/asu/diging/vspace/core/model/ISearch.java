package edu.asu.diging.vspace.core.model;

import java.util.HashSet;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface ISearch {
    
    public HashSet<Space> getSpaceSet();

    public void setSpaceSet(HashSet<Space> spaceSet);

    public HashSet<Module> getModuleSet();
    
    public void setModuleSet(HashSet<Module> moduleSet);

    public Map<String, String> getModuleFirstSlideFirstImage();

    public void setModuleFirstSlideFirstImage(Map<String, String> moduleFirstSlideFirstImage);

    public HashSet<Slide> getSlideSet();

    public void setSlideSet(HashSet<Slide> slideSet);

    public Map<String, String> getSlideFirstImage();

    public void setSlideFirstImage(Map<String, String> slideFirstImage);

    public HashSet<Slide> getSlideTextSet();

    public void setSlideTextSet(HashSet<Slide> slideTextSet);

    public Map<String, String> getSlideTextFirstImage();

    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage);

}
