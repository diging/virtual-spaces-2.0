package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchModule {

    private List<Module> moduleList;

    private Map<String, String> moduleFirstSlideFirstImage;

    private Map<String, String> showModuleAlertMessage;

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

    public Map<String, String> getShowModuleAlertMessage() {
        return showModuleAlertMessage;
    }

    public void setShowModuleAlertMessage(Map<String, String> showModuleAlertMessage) {
        this.showModuleAlertMessage = showModuleAlertMessage;
    }
   }
