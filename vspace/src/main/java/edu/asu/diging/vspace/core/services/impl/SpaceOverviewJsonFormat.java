package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.web.api.ImageApiController;
import edu.asu.diging.vspace.web.staff.ModuleController;
import edu.asu.diging.vspace.web.staff.SpaceController;

/**
 * This class is used to create nodes and edges of the space overview graph in
 * JSON format.
 * 
 * @author abiswa15
 *
 */
@Component
public class SpaceOverviewJsonFormat {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    /**
     * creating list of SpaceOverview object for space nodes in the spaceoverview
     * graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SpaceNodeList List of spaces
     * @throws JsonProcessingException
     */
    private List<SpaceOverview> constructNodesForSpaces(String contextPath, Iterable<Space> spaceNodeList)
            throws JsonProcessingException {

        List<SpaceOverview> spaceVertexList = new ArrayList<>();
        if (spaceNodeList != null) {
            for (Space spaceNode : spaceNodeList) {
                SpaceOverview spaceOverview = createSpaceNode(spaceNode, contextPath);
                spaceVertexList.add(spaceOverview);
            }
        }
        return spaceVertexList;
    }

    /**
     * creating list of SpaceOverview object for module nodes in the spaceoverview
     * graph
     * 
     * @param contextPath    This variable holds the contextpath of the application
     * @param moduleNodeList List of modules
     * @throws JsonProcessingException
     */
    private List<SpaceOverview> constructNodesForModules(String contextPath, Iterable<Module> moduleNodeList)
            throws JsonProcessingException {

        List<SpaceOverview> moduleVertexList = new ArrayList<>();

        if (moduleNodeList != null) {
            for (Module moduleNode : moduleNodeList) {
                SpaceOverview spaceOverview = createModuleNode(moduleNode, contextPath);
                moduleVertexList.add(spaceOverview);
            }
        }
        return moduleVertexList;
    }

    /**
     * Populating space node in the spaceoverview graph
     * 
     * @param space       passing the space object
     * 
     * @param contextPath This variable holds the contextpath of the application
     */
    private SpaceOverview createSpaceNode(Space space, String contextPath) {

        SpaceOverview spaceOverview = new SpaceOverview();
        spaceOverview.setName(space.getName());
        spaceOverview.setId(space.getId());
        StringBuilder linkPathBuilder = new StringBuilder();
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(SpaceController.STAFF_SPACE_PATH);
        linkPathBuilder.append(space.getId());
        spaceOverview.setLink(linkPathBuilder.toString());
        StringBuilder imagePathBuilder = new StringBuilder();
        imagePathBuilder.append(contextPath);
        imagePathBuilder.append(ImageApiController.API_IMAGE_PATH);
        imagePathBuilder.append(space.getImage().getId());
        spaceOverview.setImg(imagePathBuilder.toString());
        spaceOverview.setModule(false);
        if (space.getSpaceStatus() != null && (space.getSpaceStatus().equals(SpaceStatus.UNPUBLISHED))) {
            spaceOverview.setUnpublished(true);
        } else {
            spaceOverview.setUnpublished(false);
        }
        if (space.isHideIncomingLinks()) {
            spaceOverview.setHideIncomingLinks(true);
        } else {
            spaceOverview.setHideIncomingLinks(false);
        }
        return spaceOverview;
    }

    /**
     * Populating module node in the spaceoverview graph
     * 
     * @param module      module model object
     * 
     * @param contextPath This variable holds the contextpath of the application
     */
    private SpaceOverview createModuleNode(Module module, String contextPath) {

        SpaceOverview spaceOverview = new SpaceOverview();
        spaceOverview.setName(module.getName());
        spaceOverview.setId(module.getId());
        StringBuilder linkPathBuilder = new StringBuilder();
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(ModuleController.STAFF_MODULE_PATH);
        linkPathBuilder.append(module.getId());
        spaceOverview.setLink(linkPathBuilder.toString());
        spaceOverview.setImg("");
        spaceOverview.setModule(true);
        return spaceOverview;
    }

    /**
     * @param contextPath                 This variable holds the contextpath of the
     *                                    application
     * @param spacesToSpacesAndModulesMap
     * @return JSON with all nodes
     * @throws JsonProcessingException
     */
    public String createNodes(String contextPath, Map<String, List<String>> spacesToSpacesAndModulesMap)
            throws JsonProcessingException {

        Iterable<Module> allModulesList = moduleRepo.findAll();
        List<SpaceOverview> moduleVertexList = constructNodesForModules(contextPath, allModulesList);
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        List<SpaceOverview> spaceVertexList = constructNodesForSpaces(contextPath, allSpacesList);
        List<SpaceOverview> allVertexList = new ArrayList<>();
        for (SpaceOverview spaceVertex : spaceVertexList) {
            List<String> edges = spacesToSpacesAndModulesMap.get(spaceVertex.getId());
            if (edges != null) {
                spaceVertex.setEdges(edges);
            }
        }
        allVertexList.addAll(spaceVertexList);
        allVertexList.addAll(moduleVertexList);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(allVertexList);
    }

}
