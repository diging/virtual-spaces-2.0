package edu.asu.diging.vspace.core.services;


import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;

public interface ISequenceOverviewManager {
    
    /**
     * This method is used to fetch all Sequences based on the moduleId  which belong to a module and 
     * convert this into a SequenceOverview node. The SequenceOverviewNode is added to 
     * ModuleOverview
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */
    public ModuleOverview showModuleMap(String id);

}
