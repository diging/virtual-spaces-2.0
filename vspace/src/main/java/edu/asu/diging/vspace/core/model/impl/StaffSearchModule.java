package edu.asu.diging.vspace.core.model.impl;

import java.util.List;
import java.util.Map;

public class StaffSearchModule {

    private List<Module> modules;

    private Map<String, String> firstImageOfFirstSlideForModule;

    private Map<String, Boolean> moduleAlertMessages;

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public Map<String, String> getFirstImageOfFirstSlideForModule() {
        return firstImageOfFirstSlideForModule;
    }

    public void setFirstImageOfFirstSlideForModule(Map<String, String> firstImageOfFirstSlideForModule) {
        this.firstImageOfFirstSlideForModule = firstImageOfFirstSlideForModule;
    }

    public Map<String, Boolean> getModuleAlertMessages() {
        return moduleAlertMessages;
    }

    public void setModuleAlertMessages(Map<String, Boolean> moduleAlertMessages) {
        this.moduleAlertMessages = moduleAlertMessages;
    }

   
}
