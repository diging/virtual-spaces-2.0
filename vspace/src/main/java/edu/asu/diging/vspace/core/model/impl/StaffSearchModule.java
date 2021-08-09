package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchModule {

    private List<Module> module;

    private Map<String, String> moduleFirstSlideFirstImage;

    private Map<String, Boolean> moduleAlertMessage;

    public List<Module> getModule() {
        return module;
    }

    public void setModule(List<Module> module) {
        this.module = module;
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
