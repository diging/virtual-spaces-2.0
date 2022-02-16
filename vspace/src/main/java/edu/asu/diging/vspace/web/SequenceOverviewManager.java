package edu.asu.diging.vspace.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;

@Component
public class SequenceOverviewManager {
    
    @Autowired
    private SequenceRepository sequenceRepo;
    
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
        
        sequenceOverview = constructNodesForSequences(sequenceList);
        moduleOverviewJson.setSequenceOverview(sequenceOverview);
        return moduleOverviewJson;
    }
    
    /**
     * creating list of SequenceOverview object for sequence nodes in the moduleoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SequenceNodeList List of sequences
     */
    public List<SequenceOverview> constructNodesForSequences(List<Sequence> sequenceNodeList) {

        List<SequenceOverview> sequenceVertexList = new ArrayList<>();
        if (sequenceNodeList != null) {
            sequenceNodeList.forEach((sequenceNode)->{
                SequenceOverview sequenceOverview = createSequenceNode(sequenceNode);
                sequenceVertexList.add(sequenceOverview);
            });
        }
        return sequenceVertexList;
    }
    
    private SequenceOverview createSequenceNode(Sequence sequence) {
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence.getName());
        sequenceOverview.setId(sequence.getId());
        sequenceOverview.setSlides(sequence.getSlides());
        
        return sequenceOverview;  
    }

}
