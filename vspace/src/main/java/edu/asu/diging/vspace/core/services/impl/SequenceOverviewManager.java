package edu.asu.diging.vspace.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.ModuleOverview;
import edu.asu.diging.vspace.core.model.SequenceOverview;
import edu.asu.diging.vspace.core.model.impl.Sequence;

@Component
public class SequenceOverviewManager {
    
    @Autowired
    ModuleRepository moduleRepository;
    
    @Autowired
    ModuleLinkDisplayRepository moduleDisplayLinkRepository;
    
    @Autowired
    SequenceRepository sequenceRepo;
    
    @Autowired
    SequenceOverviewJsonFormat sequenceOverviewJsonFormat;
    
    /**
     * This method is used to fetch all Sequence which belong to a module and 
     * convert this into a SequenceOverview node. The SequenceOverviewNode is added to 
     * ModuleOverview
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */
    public ModuleOverview showModuleMap(String id) {
        List<SequenceOverview> sequenceOverview = null;
        ModuleOverview moduleOverviewJson = new ModuleOverview();
        
        List<Sequence> sequenceList = sequenceRepo.findSequencesForModule(id);
        
        sequenceOverview = sequenceOverviewJsonFormat.constructNodesForSequences(sequenceList);
        moduleOverviewJson.setSequenceOverview(sequenceOverview);
        return moduleOverviewJson;
    }

}
