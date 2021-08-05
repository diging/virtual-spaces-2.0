package edu.asu.diging.vspace.core.model;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface ISearch {
    
    public Map<String, String> getSlideFirstImage();

    public void setSlideFirstImage(Map<String, String> slideFirstImage);

    public Map<String, String> getSlideTextFirstImage();

    public void setSlideTextFirstImage(Map<String, String> slideTextFirstImage);

    List<Space> getSpaceList();

    void setSpaceList(List<Space> spaceList);

    List<Module> getModuleList();

    void setModuleList(List<Module> moduleList);

    Map<String, String> getModuleFirstSlideFirstImage();

    void setModuleFirstSlideFirstImage(Map<String, String> moduleFirstSlideFirstImage);

    List<Slide> getSlideList();

    void setSlideList(List<Slide> slideList);

    List<Slide> getSlideTextList();

    void setSlideTextList(List<Slide> slideTextList);

}
