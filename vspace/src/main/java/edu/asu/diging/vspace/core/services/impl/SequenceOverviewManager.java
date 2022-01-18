package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SequenceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ModuleOverview;
import edu.asu.diging.vspace.core.model.SequenceOverview;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;

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
     * This method is used to fetch all Sequence and corresponding slides which belong to a module.
     * 
     * @return a Map whose key is sequenceId and value is list of moduleLinks connected
     *         with the spaceId.
     */
    public Map<Sequence, List<ISlide>> getSequencesFromModules(String moduleId) {
        Map<Sequence, List<ISlide>> mapSequenceToSlides = new HashMap<Sequence, List<ISlide>>();
        List<Sequence> sequences = sequenceRepo.findSequencesForModule(moduleId);
        
        for(Sequence seq : sequences) {
            List<ISlide> sequenceSlides = seq.getSlides();
            mapSequenceToSlides.put(seq, sequenceSlides);
        }
        return mapSequenceToSlides;
        
    }
    
    public ModuleOverview showModuleMap(String id) {
        Map<Sequence,List<ISlide>> mapSequenceToSlides = getSequencesFromModules(id);
        List<Sequence> sequenceList = new ArrayList<Sequence>();
        for(Sequence seq : mapSequenceToSlides.keySet()) {
            sequenceList.add(seq);
        }
        List<SequenceOverview> sequenceOverviewJson = null;
        ModuleOverview moduleOverviewJson = new ModuleOverview();
        try {
            sequenceOverviewJson = sequenceOverviewJsonFormat.constructNodesForSequences(sequenceList);
            moduleOverviewJson.setSequenceOverview(sequenceOverviewJson);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return moduleOverviewJson;
    }

}
