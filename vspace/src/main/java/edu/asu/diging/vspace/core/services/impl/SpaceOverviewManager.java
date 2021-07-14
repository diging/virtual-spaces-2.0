package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.ISpaceOverviewManager;
import edu.asu.diging.vspace.web.api.ImageApiController;
import edu.asu.diging.vspace.web.staff.ModuleController;
import edu.asu.diging.vspace.web.staff.SpaceController;

@Service
public class SpaceOverviewManager implements ISpaceOverviewManager {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepository;

    @Autowired
    private ModuleLinkDisplayRepository moduleLinkDisplayRepository;

    /**
     * To map each space to all linked space
     */
    @Override
    public Map<String, List<ModuleLinkDisplay>> getSpaceToModuleLinks() {

        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = new HashMap<>();
        Iterable<ModuleLinkDisplay> spaceToModuleLinksList = moduleLinkDisplayRepository.findAll();

        for (ModuleLinkDisplay link : spaceToModuleLinksList) {
            String spaceId = null;
            if (link.getLink() != null && link.getLink().getSpace() != null
                    && link.getLink().getSpace().getId() != null) {

                spaceId = link.getLink().getSpace().getId();
            }
            if (spaceId != null) {
                if (!spaceToModuleLinksMap.containsKey(spaceId)) {
                    spaceToModuleLinksMap.put(spaceId, new ArrayList<>());
                }
                spaceToModuleLinksMap.get(spaceId).add(link);
            }
        }
        return spaceToModuleLinksMap;
    }

    /**
     * To map each space to all linked space
     */
    @Override
    public Map<String, List<SpaceLinkDisplay>> getSpaceToSpaceLinks() {

        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = new HashMap<>();
        Iterable<SpaceLinkDisplay> spaceToSpaceLinksList = spaceLinkDisplayRepository.findAll();
        for (SpaceLinkDisplay link : spaceToSpaceLinksList) {
            String spaceId = null;
            if (link.getLink() != null && link.getLink().getSourceSpace() != null
                    && link.getLink().getSourceSpace().getId() != null) {

                spaceId = link.getLink().getSourceSpace().getId();
            }
            if (spaceId != null) {
                if (!spaceToSpaceLinksMap.containsKey(spaceId)) {
                    spaceToSpaceLinksMap.put(spaceId, new ArrayList<>());
                }
                spaceToSpaceLinksMap.get(spaceId).add(link);
            }
        }
        return spaceToSpaceLinksMap;
    }

    /**
     * To retrieve all space to space and space to module links corresponding to
     * each space
     * 
     * @param contextPath           The contextpath of the application is being
     *                              passed
     * @param mapper                Passing this object to create object node for
     *                              creating json of space and module node in the
     *                              spaceoverview graph
     * @param nodeArray             To create the json for each node of
     *                              spaceoverview graph
     * @param spaceLinkMap          For holding list of all links corresponding to
     *                              each space
     * @param spaceToModuleLinksMap For getting list of links from one space to
     *                              other modules
     * @param spaceToSpaceLinksMap  For getting list of links from one space to
     *                              other spaces
     */
    @Override
    public void getSpaceLinkMap(String contextPath, ObjectMapper mapper, ArrayNode nodeArray,
            Map<String, List<String>> spaceLinkMap, Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap) {

        Iterable<Space> allSpacesList = spaceRepo.findAll();
        if (allSpacesList != null) {
            createSpaceNodeInOverviewGraph(contextPath, mapper, nodeArray);
            for (Space space : allSpacesList) {
                List<ModuleLinkDisplay> spaceToModulelinksList = spaceToModuleLinksMap.get(space.getId());
                List<SpaceLinkDisplay> spaceLinks = spaceToSpaceLinksMap.get(space.getId());
                List<SpaceLinkDisplay> spaceToSpaceLinksList = new ArrayList<>();
                if (spaceLinks != null) {
                    spaceToSpaceLinksList.addAll(spaceLinks);
                }
                List<String> spaceAllLink = new ArrayList<>();

                if (spaceToModulelinksList != null) {
                    spaceToModulelinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getModule() != null
                                && link.getLink().getModule().getId() != null) {
                            spaceAllLink.add(link.getLink().getModule().getId());
                        }
                    });
                }
                if (spaceToSpaceLinksList != null) {
                    spaceToSpaceLinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getTarget() != null
                                && link.getLink().getTarget().getId() != null) {
                            spaceAllLink.add(link.getLink().getTarget().getId());
                        }
                    });
                }
                if (space.getId() != null) {
                    spaceLinkMap.put(space.getId(), spaceAllLink);
                }
            }
        }
    }

    /**
     * creating json for space and module node in the spaceoverview graph
     * 
     * @param contextPath This variable holds the contextpath of the application
     * @param nodeList    List of space/module
     * @param mapper      For creating ObjectNode
     * @param nodeArray   Creating Array of ObjectNode for nodes in spaceoverview
     *                    graph
     */
    @Override
    public void createNodeForOverviewGraph(String contextPath, Iterable<? extends IVSpaceElement> nodeList,
            ObjectMapper mapper, ArrayNode nodeArray) {

        if (nodeList != null) {
            StringBuilder linkPathBuilder = new StringBuilder();
            StringBuilder imagePathBuilder = new StringBuilder();
            for (IVSpaceElement nodeval : nodeList) {
                if (nodeval != null) {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("name", nodeval.getName());
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

    @Override
    public void createModuleNodeInOverviewGraph(String contextPath, ObjectMapper mapper, ArrayNode nodeArray) {
        Iterable<Module> allModulesList = moduleRepo.findAll();
        createNodeForOverviewGraph(contextPath, allModulesList, mapper, nodeArray);
    }

    @Override
    public void createSpaceNodeInOverviewGraph(String contextPath, ObjectMapper mapper, ArrayNode nodeArray) {
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        createNodeForOverviewGraph(contextPath, allSpacesList, mapper, nodeArray);
    }

    /**
     * creating json for edge between nodes in the spaceoverview graph
     * 
     * @param mapper       For creating ObjectNode
     * @param linkArray    Creating Array of ObjectNode for edge between nodes in
     *                     the spaceoverview graph
     * @param spaceLinkMap List of all links corresponding to each space
     */
    @Override
    public void createLinkForOverviewGraph(ObjectMapper mapper, ArrayNode linkArray,
            Map<String, List<String>> spaceLinkMap) {

        for (Entry<String, List<String>> entry : spaceLinkMap.entrySet()) {
            for (String value : entry.getValue()) {
                ObjectNode link = mapper.createObjectNode();
                link.put("target", value);
                link.put("source", entry.getKey());
                linkArray.add(link);
            }
        }

    }

    /**
     * creating json for space node in the spaceoverview graph
     * 
     * @param linkPathBuilder  StringBuilder object to create the URL link for space
     *                         in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param isModule         boolean variable to indicate whether it is module or
     *                         not
     * @param space            passing the space object
     * @param node             creating json for space node in the spaceoverview
     *                         graph
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     * @param spaceId          This variable is holding the space id corresponding
     *                         each space in vspace
     */
    @Override
    public void createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space,
            ObjectNode node, String contextPath, String spaceId) {
        node.put("id", spaceId);
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(SpaceController.STAFF_SPACE_PATH);
        linkPathBuilder.append(spaceId);
        node.put("link", linkPathBuilder.toString());
        imagePathBuilder.setLength(0);
        imagePathBuilder.append(contextPath);
        imagePathBuilder.append(ImageApiController.API_IMAGE_PATH);
        imagePathBuilder.append(space.getImage().getId());
        node.put("img", imagePathBuilder.toString());
        node.put("isModule", false);

        if (space.getSpaceStatus() != null && (space.getSpaceStatus().equals(SpaceStatus.UNPUBLISHED))) {
            node.put("isUnpublished", true);
        } else {
            node.put("isUnpublished", false);
        }
        if (space.isHideIncomingLinks()) {
            node.put("isHideIncomingLinks", true);
        } else {
            node.put("isHideIncomingLinks", false);
        }
    }

    /**
     * creating json for module node in the spaceoverview graph
     * 
     * @param linkPathBuilder  StringBuilder object to create the URL link for
     *                         module in staff side
     * @param imagePathBuilder StringBuilder object to create the URL link for image
     *                         in staff side
     * @param isModule         boolean variable to indicate whether it is module or
     *                         not
     * @param node             creating json for module node in the spaceoverview
     *                         graph
     * @param contextPath      This variable holds the contextpath of the
     *                         application
     * @param moduleId         This variable is holding the module id corresponding
     *                         each module in vspace
     */
    @Override
    public void createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, ObjectNode node,
            String contextPath, String moduleId) {
        node.put("id", moduleId);
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(ModuleController.STAFF_MODULE_PATH);
        linkPathBuilder.append(moduleId);
        node.put("link", linkPathBuilder.toString());
        node.put("img", "");
        node.put("isModule", true);
    }
}
