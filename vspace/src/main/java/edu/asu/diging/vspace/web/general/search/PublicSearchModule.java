package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.impl.Module;

public class PublicSearchModule {
    
    private List<Module> moduleList;
    
    private  Map<String,String> moduleFirstSlideFirstImage;
    
    private Map<String, Boolean> moduleAlertMessage;

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

    public Map<String, Boolean> getModuleAlertMessage() {
        return moduleAlertMessage;
    }

    public void setModuleAlertMessage(Map<String, Boolean> moduleAlertMessage) {
        this.moduleAlertMessage = moduleAlertMessage;
    }
    
}