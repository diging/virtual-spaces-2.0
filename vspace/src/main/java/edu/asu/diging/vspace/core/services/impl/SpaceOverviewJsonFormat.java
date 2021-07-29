package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.SpaceOverview;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.web.api.ImageApiController;
import edu.asu.diging.vspace.web.staff.ModuleController;
import edu.asu.diging.vspace.web.staff.SpaceController;

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
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (Space spaceNode : spaceNodeList) {
                SpaceOverview spaceOverview = createSpaceNode(linkPathBuilder, imagePathBuilder, spaceNode,
                        contextPath);
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
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (Module moduleNode : moduleNodeList) {
                SpaceOverview spaceOverview = createModuleNode(linkPathBuilder, imagePathBuilder, moduleNode,
                        contextPath);
                moduleVertexList.add(spaceOverview);
            }
        }
        return moduleVertexList;
    }

    /**
     * Populating space node in the spaceoverview graph
     * 
     * @param linkPathBuilder  StringBuilder object to create the URL link for space
     *                         in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param space            passing the space object
     * 
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     */
    private SpaceOverview createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space,
            String contextPath) {

        SpaceOverview spaceOverview = new SpaceOverview();
        spaceOverview.setName(space.getName());
        spaceOverview.setId(space.getId());
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(SpaceController.STAFF_SPACE_PATH);
        linkPathBuilder.append(space.getId());
        spaceOverview.setLink(linkPathBuilder.toString());
        imagePathBuilder.setLength(0);
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
     * @param linkPathBuilder  StringBuilder object to create the URL link for
     *                         module in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param module           module model object
     * 
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     */
    private SpaceOverview createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Module module,
            String contextPath) {

        SpaceOverview spaceOverview = new SpaceOverview();
        spaceOverview.setName(module.getName());
        spaceOverview.setId(module.getId());
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(ModuleController.STAFF_MODULE_PATH);
        linkPathBuilder.append(module.getId());
        spaceOverview.setLink(linkPathBuilder.toString());
        spaceOverview.setImg("");
        spaceOverview.setModule(true);
        return spaceOverview;
    }

    /**
     * @param contextPath This variable holds the contextpath of the application
     * @return Json corresponding to nodeArray
     * @throws JsonProcessingException
     */
    public String createNodes(String contextPath) throws JsonProcessingException {

        Iterable<Module> allModulesList = moduleRepo.findAll();
        List<SpaceOverview> moduleVertexList = constructNodesForModules(contextPath, allModulesList);
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        List<SpaceOverview> spaceVertexList = constructNodesForSpaces(contextPath, allSpacesList);
        List<SpaceOverview> allVertexList = new ArrayList<>();
        allVertexList.addAll(spaceVertexList);
        allVertexList.addAll(moduleVertexList);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(allVertexList);
    }

    /**
     * creating json for edge between nodes in the spaceoverview graph
     * 
     * @param spacesToToSpacesAndModulesMap List of all links corresponding to each
     *                                      space
     * @return Json corresponding to linkArray
     * @throws JsonProcessingException
     */
    public String createEdges(Map<String, List<String>> spacesToToSpacesAndModulesMap) throws JsonProcessingException {

        List<SpaceOverview> edgeList = new ArrayList<>();
        for (Entry<String, List<String>> entry : spacesToToSpacesAndModulesMap.entrySet()) {
            for (String value : entry.getValue()) {
                SpaceOverview spaceOverview = new SpaceOverview();
                spaceOverview.setTarget(value);
                spaceOverview.setSource(entry.getKey());
                edgeList.add(spaceOverview);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(edgeList);
    }

}
