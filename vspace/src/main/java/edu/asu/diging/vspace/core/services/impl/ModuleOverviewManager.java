package edu.asu.diging.vspace.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;

@Component
public class ModuleOverviewManager {
    
    @Autowired
    ModuleRepository moduleRepository;
    
    @Autowired
    ModuleLinkDisplayRepository moduleDisplayLinkRepository;
    
    @Autowired
    SequenceRepository sequenceRepo;
    
    
    /**
     * This method is used to fetch all Sequence and corresponding slides which belong to a module.
     * 
     * @return a Map whose key is sequenceId and value is list of moduleLinks connected
     *         with the spaceId.
     */
    public Map<String, List<ISlide>> getSequencesFromModules(String moduleId) {
        Map<String, List<ISlide>> mapSequenceToSlides = new HashMap<String, List<ISlide>>();
        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        
        for(Sequence seq : sequences) {
            String sequenceId = seq.getId();
            List<ISlide> sequenceSlides = seq.getSlides();
            mapSequenceToSlides.put(sequenceId, sequenceSlides);
        }
        return mapSequenceToSlides;
        
    }

}
