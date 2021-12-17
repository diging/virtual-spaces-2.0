package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.SequenceOverview;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.services.impl.SequenceOverviewJsonFormat;
import edu.asu.diging.vspace.core.services.impl.SequenceOverviewManager;

@Controller
public class SequenceOverviewController {
    
    public static final String STAFF_MODULE_PATH = "/staff/module/showmodulemap/";
    
    @Autowired
    SequenceOverviewManager sequenceOverviewManager;
    
    @Autowired
    SequenceOverviewJsonFormat sequenceOverviewJsonFormat;
    
    @RequestMapping(STAFF_MODULE_PATH+"{id}")
    public String showModuleMap(@PathVariable String id, Model model) {
        Map<Sequence,List<ISlide>> mapSequenceToSlides = sequenceOverviewManager.getSequencesFromModules(id);
        List<Sequence> sequenceList = new ArrayList<Sequence>();
        for(Sequence seq : mapSequenceToSlides.keySet()) {
            sequenceList.add(seq);
        }
        try {
            List<SequenceOverview> sequenceOverviewJson = sequenceOverviewJsonFormat.constructNodesForSequences(sequenceList);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("mapSequencesToSlides", mapSequenceToSlides);
        return "";
    }
    
    
    
}
