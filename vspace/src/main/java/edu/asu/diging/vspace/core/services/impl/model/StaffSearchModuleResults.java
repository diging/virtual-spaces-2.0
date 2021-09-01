package edu.asu.diging.vspace.core.services.impl.model;

import java.util.List;
import java.util.Map;
import edu.asu.diging.vspace.core.model.impl.Module;

public class StaffSearchModuleResults {

    private List<Module> modules;

    private Map<String, String> moduleImageIdMap;

    private Map<String, Boolean> moduleAlertMessages;

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
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
