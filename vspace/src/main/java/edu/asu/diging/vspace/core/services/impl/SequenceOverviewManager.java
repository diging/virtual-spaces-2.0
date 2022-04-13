package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceManager;
import edu.asu.diging.vspace.core.services.ISequenceOverviewManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;
import edu.asu.diging.vspace.core.services.impl.model.SlideOverview;

@Service
public class SequenceOverviewManager implements ISequenceOverviewManager {
    
    @Autowired
    private SequenceRepository sequenceRepo;
    
    @Autowired
    private ISequenceManager sequenceManager;
    
    @Autowired
    private IModuleManager moduleManager;
    
    /**
     * This method is used to fetch all Sequences which belong to a module and 
     * convert this into a SequenceOverview node. The SequenceOverviewNode is added to 
     * ModuleOverview
     * @return ModuleOverview which contains the module and the list of sequences and its slides
     */
    public ModuleOverview showModuleMap(String id) {
        IModule module = moduleManager.getModule(id);
        ISequence startSequence = module.getStartSequence();
        List<ISequence> sequences = module.getSequences();
        SequenceOverview startSequenceNode = createSequenceOverviewNode(startSequence);
        List<SequenceOverview> otherSequences = new ArrayList<SequenceOverview>();
        for(ISequence sequence : sequences) {
            if(sequence != startSequence) {
                otherSequences.add(createSequenceOverviewNode(sequence));
            }
        }
        ModuleOverview moduleOverviewJson = new ModuleOverview();
        moduleOverviewJson.setStartSequence(startSequenceNode);
        return moduleOverviewJson;
    }
    
    /**
     * creating list of SequenceOverview object for sequence nodes in the moduleoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SequenceNodeList List of sequences
     */
    private SequenceOverview constructNodesFromStartSequence(ISequence startSequence, List<ISequence> sequences) {
        Set<BranchingPoint> branchingPointsMap = new HashSet<BranchingPoint>();
        SequenceOverview startSequenceNode = createSequenceOverviewNode(startSequence);
        List<SequenceOverview> otherSequences = new ArrayList<SequenceOverview>();
        for(ISequence sequence : sequences) {
            if(sequence != startSequence) {
                otherSequences.add(createSequenceOverviewNode(sequence));
            }
        }
        
    }
    
    
    private List<SequenceOverview> constructSequenceNodes(Sequence sequence, Set<BranchingPoint> branchingPointsMap){
        List<ISlide> sequenceSlides = sequence.getSlides();
        for(ISlide slide : sequenceSlides) {
            if(slide instanceof BranchingPoint ) {
                branchingPointsMap.add((BranchingPoint)slide);
            }
        }
    }
    
    private SequenceOverview createSequenceOverviewNode(ISequence sequence) {
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence.getName());
        sequenceOverview.setId(sequence.getId());
        List<SlideOverview> slideOverviews = createSlideOverviewNode(sequence.getSlides());
        sequenceOverview.setSlides(slideOverviews);
        return sequenceOverview;  
    }
    
    private List<SlideOverview> createSlideOverviewNode(List<ISlide> slides){
        List<SlideOverview> slideOverviews = new ArrayList<SlideOverview>();
        for(ISlide slide : slides) {
            SlideOverview slideOverview = new SlideOverview(); 
            if(slide instanceof BranchingPoint ) {
                slideOverview.setBranchingPoint(true);
            }
            slideOverview.setId(slide.getId());
            slideOverview.setName(slide.getName());
            List<ISequence> sequenceChoices = ((BranchingPoint)slide).getSequence();
            List<String> slideOverviewSequenceChoices = new ArrayList<String>();
            for(ISequence sequence : sequenceChoices) {
                slideOverviewSequenceChoices.add(sequence.getName());
            }
            slideOverview.setSequenceIds(slideOverviewSequenceChoices);
            slideOverviews.add(slideOverview);
        } 
        return slideOverviews;
    }

}
