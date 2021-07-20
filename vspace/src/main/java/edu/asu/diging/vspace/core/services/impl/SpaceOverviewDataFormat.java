package edu.asu.diging.vspace.core.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
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
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SpaceOverviewDataFormat {

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

    private ArrayNode nodeArray;

    private ArrayNode linkArray;

    public SpaceOverviewDataFormat() {
        this.nodeArray = JsonHelper.getMapper().createArrayNode();
        this.linkArray = JsonHelper.getMapper().createArrayNode();
    }

    /**
     * creating ArrayNode for module node in the spaceoverview graph
     * 
     * @param contextPath This variable holds the contextpath of the application
     * 
     * @throws JsonProcessingException
     */
    public void createModuleNodeInOverviewGraph(String contextPath) throws JsonProcessingException {
        Iterable<Module> allModulesList = moduleRepo.findAll();
        constructNodeArray(contextPath, allModulesList);
    }

    /**
     * creating ArrayNode for space node in the spaceoverview graph
     * 
     * @param contextPath This variable holds the contextpath of the application
     * 
     * @throws JsonProcessingException
     */
    public void createSpaceNodeInOverviewGraph(String contextPath) throws JsonProcessingException {
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        constructNodeArray(contextPath, allSpacesList);
    }

    /**
     * creating ArrayNode for space and module node in the spaceoverview graph
     * 
     * @param contextPath This variable holds the contextpath of the application
     * @param nodeList    List of space/module
     * 
     * @throws JsonProcessingException
     */
    public void constructNodeArray(String contextPath, Iterable<? extends IVSpaceElement> nodeList)
            throws JsonProcessingException {

        if (nodeList != null) {
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (IVSpaceElement nodeval : nodeList) {
                if (nodeval != null) {
                    ObjectNode node = JsonHelper.getMapper().createObjectNode();
                    node.put(NAME, nodeval.getName());
                    String id = nodeval.getId();
                    if (nodeval instanceof Space) {
                        createSpaceNode(linkPathBuilder, imagePathBuilder, (Space) nodeval, node, contextPath, id);
                    }
                    if (nodeval instanceof Module) {
                        createModuleNode(linkPathBuilder, imagePathBuilder, node, contextPath, id);
                    }
                    nodeArray.add(node);
                }
            }
        }
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
    public void createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space,
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
    public void createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, ObjectNode node,
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
     * @param contextPath   This variable holds the contextpath of the
     *                      application
     * @return Json corresponding to nodeArray
     * @throws JsonProcessingException
     */
    public String createNodeForOverviewGraph(String contextPath) throws JsonProcessingException {
        createModuleNodeInOverviewGraph(contextPath);
        createSpaceNodeInOverviewGraph(contextPath);
        return JsonHelper.convertToJson(nodeArray);
    }

    /**
     * creating json for edge between nodes in the spaceoverview graph
     * 
     * @param spaceLinkMap List of all links corresponding to each space
     * @return Json corresponding to linkArray
     * @throws JsonProcessingException
     */
    public String createLinkForOverviewGraph(Map<String, List<String>> spaceLinkMap) throws JsonProcessingException {

        for (Entry<String, List<String>> entry : spaceLinkMap.entrySet()) {
            for (String value : entry.getValue()) {
                ObjectNode link = JsonHelper.getMapper().createObjectNode();
                link.put(TARGET, value);
                link.put(SOURCE, entry.getKey());
                linkArray.add(link);
            }
        }
        return JsonHelper.convertToJson(linkArray);
    }

}
