package edu.asu.diging.vspace.web.general.search;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.IModule;

public class PublicSearchModuleResults {
    
    private List<IModule> modules;
    
    private  Map<String,String> moduleImageIdMap;
    
    private Map<String, Boolean> moduleAlertMessages;

    public List<IModule> getModules() {
        return modules;
    }

    public void setModules(List<IModule> modules) {
        this.modules = modules;
    }

    public Map<String, String> getModuleImageIdMap() {
        return moduleImageIdMap;
    }

    public void setModuleImageIdMap(Map<String, String> moduleImageIdMap) {
        this.moduleImageIdMap = moduleImageIdMap;
    }

    public Map<String, Boolean> getModuleAlertMessages() {
        return moduleAlertMessages;
    }

    public void setModuleAlertMessages(Map<String, Boolean> moduleAlertMessages) {
        this.moduleAlertMessages = moduleAlertMessages;
    }

}