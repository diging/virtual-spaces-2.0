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
public class ModuleOverviewJsonFormat {
    
    @Autowired
    private ModuleRepository moduleRepo;
    
    /**
     * creating list of ModuleOverview object for space nodes in the moduleoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param ModuleNodeList List of module
     * @throws JsonProcessingException
     */
    private List<ModuleOverview> constructNodesForSpaces(String contextPath, Iterable<Module> moduleNodeList)
            throws JsonProcessingException {

        List<ModuleOverview> moduleVertexList = new ArrayList<>();
        if (moduleNodeList != null) {
            for (Module spaceNode : moduleNodeList) {
               // ModuleOverview spaceOverview = createModuleNode(spaceNode, contextPath);
               // moduleVertexList.add(spaceOverview);
            }
        }
        return moduleVertexList;
    }

}
