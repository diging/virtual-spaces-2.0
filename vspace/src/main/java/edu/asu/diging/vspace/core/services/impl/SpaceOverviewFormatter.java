package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.util.JsonHelper;
import edu.asu.diging.vspace.web.api.ImageApiController;
import edu.asu.diging.vspace.web.staff.ModuleController;
import edu.asu.diging.vspace.web.staff.SpaceController;

@Component
public class SpaceOverviewFormatter {

    private static final String SOURCE = "source";

    private static final String TARGET = "target";

    private static final String NAME = "name";

    private static final String LINK = "link";

    private static final String ID = "id";

    private static final String IMG = "img";

    private static final String IS_MODULE = "isModule";

    private static final String IS_HIDE_INCOMING_LINKS = "isHideIncomingLinks";

    private static final String IS_UNPUBLISHED = "isUnpublished";

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private JsonHelper jsonHelper;

    /**
     * creating ArrayNode for space and module node in the spaceoverview graph
     * 
     * @param contextPath   This variable holds the contextpath of the application
     * @param SpaceNodeList List of spaces
     * @param nodeArray     This gets populated with space node in the spaceoverview
     *                      graph
     * @throws JsonProcessingException
     */
    private void constructSpaceNodeArray(String contextPath, Iterable<Space> spaceNodeList, ArrayNode nodeArray)
            throws JsonProcessingException {

        if (spaceNodeList != null) {
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (Space spaceNode : spaceNodeList) {
                ObjectNode node = createNodeAndSetName(spaceNode);
                String id = spaceNode.getId();
                createSpaceNode(linkPathBuilder, imagePathBuilder, spaceNode, node, contextPath, id);
                nodeArray.add(node);
            }
        }
    }

    /**
     * creating ArrayNode for space and module node in the spaceoverview graph
     * 
     * @param contextPath    This variable holds the contextpath of the application
     * @param moduleNodeList List of modules
     * @param nodeArray      This gets populated with module node in the
     *                       spaceoverview graph
     * @throws JsonProcessingException
     */
    private void constructModuleNodeArray(String contextPath, Iterable<Module> moduleNodeList, ArrayNode nodeArray)
            throws JsonProcessingException {

        if (moduleNodeList != null) {
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (Module moduleNode : moduleNodeList) {
                ObjectNode node = createNodeAndSetName(moduleNode);
                String id = moduleNode.getId();
                createModuleNode(linkPathBuilder, imagePathBuilder, node, contextPath, id);
                nodeArray.add(node);
            }
        }
    }

    /**
     * This is a common method for both space and module
     * 
     * @param nodeval This variable store either space/module node
     * @return After setting name variable of node with spacename/modulename it returns the node
     */
    private ObjectNode createNodeAndSetName(IVSpaceElement nodeval) {

        ObjectNode node = jsonHelper.getMapper().createObjectNode();
        node.put(NAME, nodeval.getName());
        return node;
    }

    /**
     * Populating space node in the spaceoverview graph
     * 
     * @param linkPathBuilder  StringBuilder object to create the URL link for space
     *                         in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param space            passing the space object
     * @param node             creating json for space node in the spaceoverview
     *                         graph
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     * @param spaceId          This variable is holding the space id corresponding
     *                         each space in vspace
     */
    private void createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space,
            ObjectNode node, String contextPath, String spaceId) {

        node.put(ID, spaceId);
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(SpaceController.STAFF_SPACE_PATH);
        linkPathBuilder.append(spaceId);
        node.put(LINK, linkPathBuilder.toString());
        imagePathBuilder.setLength(0);
        imagePathBuilder.append(contextPath);
        imagePathBuilder.append(ImageApiController.API_IMAGE_PATH);
        imagePathBuilder.append(space.getImage().getId());
        node.put(IMG, imagePathBuilder.toString());
        node.put(IS_MODULE, false);

        if (space.getSpaceStatus() != null && (space.getSpaceStatus().equals(SpaceStatus.UNPUBLISHED))) {
            node.put(IS_UNPUBLISHED, true);
        } else {
            node.put(IS_UNPUBLISHED, false);
        }
        if (space.isHideIncomingLinks()) {
            node.put(IS_HIDE_INCOMING_LINKS, true);
        } else {
            node.put(IS_HIDE_INCOMING_LINKS, false);
        }
    }

    /**
     * Populating module node in the spaceoverview graph
     * 
     * @param linkPathBuilder  StringBuilder object to create the URL link for
     *                         module in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param node             creating json for module node in the spaceoverview
     *                         graph
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     * @param moduleId         This variable is holding the module id corresponding
     *                         each module in vspace
     */
    private void createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, ObjectNode node,
            String contextPath, String moduleId) {

        node.put(ID, moduleId);
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(ModuleController.STAFF_MODULE_PATH);
        linkPathBuilder.append(moduleId);
        node.put(LINK, linkPathBuilder.toString());
        node.put(IMG, "");
        node.put(IS_MODULE, true);
    }

    /**
     * @param contextPath This variable holds the contextpath of the application
     * @return Json corresponding to nodeArray
     * @throws JsonProcessingException
     */
    public String createNodes(String contextPath) throws JsonProcessingException {

        ArrayNode nodeArray = jsonHelper.getMapper().createArrayNode();
        Iterable<Module> allModulesList = moduleRepo.findAll();
        constructModuleNodeArray(contextPath, allModulesList, nodeArray);
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        constructSpaceNodeArray(contextPath, allSpacesList, nodeArray);
        return jsonHelper.convertToJson(nodeArray);
    }

    /**
     * creating json for edge between nodes in the spaceoverview graph
     * 
     * @param spaceLinkedToToSpacesAndModulesMap List of all links corresponding to
     *                                           each space
     * @return Json corresponding to linkArray
     * @throws JsonProcessingException
     */
    public String createEdges(Map<String, List<String>> spaceLinkedToToSpacesAndModulesMap)
            throws JsonProcessingException {

        ArrayNode linkArray = jsonHelper.getMapper().createArrayNode();
        for (Entry<String, List<String>> entry : spaceLinkedToToSpacesAndModulesMap.entrySet()) {
            for (String value : entry.getValue()) {
                ObjectNode link = jsonHelper.getMapper().createObjectNode();
                link.put(TARGET, value);
                link.put(SOURCE, entry.getKey());
                linkArray.add(link);
            }
        }
        return jsonHelper.convertToJson(linkArray);
    }

}
