package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.BranchingPoint;
import edu.asu.diging.vspace.core.services.IModuleManager;
import edu.asu.diging.vspace.core.services.ISequenceOverviewManager;
import edu.asu.diging.vspace.core.services.impl.model.ModuleOverview;
import edu.asu.diging.vspace.core.services.impl.model.SequenceOverview;
import edu.asu.diging.vspace.core.services.impl.model.SlideOverview;

@Service
public class SequenceOverviewManager implements ISequenceOverviewManager {
    
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
        
        List<ISequence> sequences = moduleManager.getModuleSequences(id);
        
        SequenceOverview startSequenceNode = createSequenceOverviewNode(startSequence);
        List<SequenceOverview> otherSequences = new ArrayList<SequenceOverview>();
        for(ISequence sequence : sequences) {
            
            if(sequence != startSequence) {
                otherSequences.add(createSequenceOverviewNode(sequence));
            }
        }
        ModuleOverview moduleOverviewJson = new ModuleOverview();
        moduleOverviewJson.setStartSequence(startSequenceNode);
        moduleOverviewJson.setOtherSequences(otherSequences);
        return moduleOverviewJson;
    }
    
    private SequenceOverview createSequenceOverviewNode(ISequence sequence) {
        
        SequenceOverview sequenceOverview = new SequenceOverview();
        sequenceOverview.setName(sequence.getName());
        sequenceOverview.setId(sequence.getId());
        List<SlideOverview> slideOverviews = createSlideOverviewNode(sequence.getSlides());
        sequenceOverview.setSlideOverviews(slideOverviews);
        return sequenceOverview;  
    }
    
    private List<SlideOverview> createSlideOverviewNode(List<ISlide> slides){
        List<SlideOverview> slideOverviews = new ArrayList<SlideOverview>();
        for(ISlide slide : slides) {
            SlideOverview slideOverview = new SlideOverview(); 
            if(slide instanceof BranchingPoint ) {
                slideOverview.setBranchingPoint(true);
                List<IChoice> sequenceChoices = ((BranchingPoint)slide).getChoices();
                List<String> slideOverviewSequenceChoices = new ArrayList<String>();
                for(IChoice choice : sequenceChoices) {
                    slideOverviewSequenceChoices.add(choice.getSequence().getName());
                }
                slideOverview.setSequenceIds(slideOverviewSequenceChoices);
            }
            slideOverview.setId(slide.getId());
            slideOverview.setName(slide.getName());
            slideOverviews.add(slideOverview);
            
        } 
        return slideOverviews;
    }

}
