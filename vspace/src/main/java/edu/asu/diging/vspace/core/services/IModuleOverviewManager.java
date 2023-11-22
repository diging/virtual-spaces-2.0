package edu.asu.diging.vspace.core.services;


import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;

public interface IModuleOverviewManager {
    
    /**
     * This method returns a ModuleOverview based on a module Id. It fetches all Sequences which belong to the module and 
     * convert this into a SequenceOverview node. This SequenceOverviewNode is added to 
     * ModuleOverview
     * @param id
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */
    public ModuleOverview getModuleOverview(String moduleId);

}
