package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.ModuleOverview;

@Component
public class SequenceOverviewJsonFormat {
    
    @Autowired
    private ModuleRepository moduleRepo;
    
    /**
     * creating list of SequenceOverview object for sequence nodes in the moduleoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SequenceNodeList List of sequences
     * @throws JsonProcessingException
     */
    private List<ModuleOverview> constructNodesForModules(String contextPath, Iterable<Module> sequenceNodeList)
            throws JsonProcessingException {

        List<ModuleOverview> moduleVertexList = new ArrayList<>();
        if (sequenceNodeList != null) {
            for (Module spaceNode : sequenceNodeList) {
               // ModuleOverview spaceOverview = createModuleNode(spaceNode, contextPath);
               // moduleVertexList.add(spaceOverview);
            }
        }
        return moduleVertexList;
    }

}
