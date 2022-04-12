package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.ISequenceOverviewManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;

@Service
public class SequenceOverviewManager implements ISequenceOverviewManager {
    
    @Autowired
    private SequenceRepository sequenceRepo;
    
    @Autowired
    private SequenceManager sequenceManager;
    
    @Autowired
    private ModuleManager moduleManager;
    
    /**
     * This method is used to fetch all Sequences which belong to a module and 
     * convert this into a SequenceOverview node. The SequenceOverviewNode is added to 
     * ModuleOverview
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */
    public ModuleOverview showModuleMap(String id) {
        
        IModule module = moduleManager.getModule(id);
        ISequence startSequence = module.getStartSequence();
        List<SequenceOverview> sequenceOverview = constructNodesFromStartSequence(startSequence);
        ModuleOverview moduleOverviewJson = new ModuleOverview();
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
    private List<SequenceOverview> constructNodesFromStartSequence(ISequence startSequence) {
        List<ISlide> slides = startSequence.getSlides();
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(startSequence.getName());
        sequenceOverview.setId(startSequence.getId());
        List<IBranchingPoint> branchingPoints = new ArrayList<IBranchingPoint>();
        List<ISlide> sequenceSlides = new ArrayList<ISlide>();
        for(ISlide slide : slides) {
            if(slide instanceof BranchingPoint ) {
                branchingPoints.add((IBranchingPoint)slide);
            }else {
                sequenceSlides.add(slide);
            }
        }
        sequenceOverview.setSlides(sequenceSlides);
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
