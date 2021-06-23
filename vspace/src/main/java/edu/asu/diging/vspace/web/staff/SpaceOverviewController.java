package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import edu.asu.diging.vspace.web.api.ImageApiController;

/**
 * 
 * @author Avirup Biswas
 *
 */
@Controller
public class SpaceOverviewController {
    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepository;

    @Autowired
    private ModuleLinkDisplayRepository moduleLinkDisplayRepository;

    @RequestMapping("/staff/overview")
    public String spaceOverview(HttpServletRequest request, Model model) throws JsonProcessingException {

        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();

        Iterable<ModuleLinkDisplay> spaceToModuleLinks = moduleLinkDisplayRepository.findAll();
        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = new HashMap<>();
        getSpaceToModuleLinks(spaceToModuleLinks, spaceToModuleLinksMap);

        Iterable<SpaceLinkDisplay> spaceToSpaceLinksList = spaceLinkDisplayRepository.findAll();
        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = new HashMap<>();
        getSpaceToSpaceLinks(spaceToSpaceLinksList, spaceToSpaceLinksMap);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();

        Iterable<Space> allSpacesList = spaceRepo.findAll();
        List<String> allNodeList = new ArrayList<>();
        getSpaceLinkMap(request.getContextPath(), allSpacesList, mapper, nodeArray, allNodeList, spaceLinkMap,
                spaceToModuleLinksMap, spaceToSpaceLinksMap);

        Iterable<Module> allModulesList = moduleRepo.findAll();
        createNodeForOverviewGraph(request.getContextPath(), allModulesList, mapper, nodeArray, allNodeList);

        createEdgeForOverviewGraph(mapper, linkArray, allNodeList, spaceLinkMap);
        model.addAttribute("overviewNode", mapper.writeValueAsString(nodeArray));
        model.addAttribute("overviewLink", mapper.writeValueAsString(linkArray));
        return "staff/spaces/graph";
    }

    /**
     * To retrieve all space to space and space to module links corresponding to
     * each space
     * 
     * @param contextPath           The contextpath of the application is being
     *                              passed
     * @param allSpacesList         List of all spaces which are available in vspace
     *                              is being passed
     * @param mapper                Passing this object to create object node for
     *                              creating json of space and module node in the
     *                              spaceoverview graph
     * @param nodeArray             To create the json for each node of
     *                              spaceoverview graph
     * @param allNodesList          List of all the nodes of spaceoverview graph
     * @param spaceLinkMap          For holding list of all links corresponding to
     *                              each space
     * @param spaceToModuleLinksMap For getting list of links from one space to
     *                              other modules
     * @param spaceToSpaceLinksMap  For getting list of links from one space to
     *                              other spaces
     */
    private void getSpaceLinkMap(String contextPath, Iterable<Space> allSpacesList, ObjectMapper mapper,
            ArrayNode nodeArray, List<String> allNodesList, Map<String, List<String>> spaceLinkMap,
            Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap) {
        if (allSpacesList != null) {
            createNodeForOverviewGraph(contextPath, allSpacesList, mapper, nodeArray, allNodesList);
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
     * To map each space to all linked space
     * 
     * @param spaceToSpaceLinksList passing list of space to space link
     * @param spaceToSpaceLinksMap  To hold list of space to space link
     *                              corresponding to each space
     */
    private void getSpaceToSpaceLinks(Iterable<SpaceLinkDisplay> spaceToSpaceLinksList,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap) {

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
    }

    /**
     * To map each space to all linked space
     * 
     * @param spaceToModuleLinksList passing list of module to space link
     * @param spaceToModuleLinksMap  To hold list of space to module link
     *                               corresponding to each space
     */
    private void getSpaceToModuleLinks(Iterable<ModuleLinkDisplay> spaceToModuleLinksList,
            Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap) {

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
    }

    /**
     * creating json for space and module node in the spaceoverview graph
     * 
     * @param contextPath This variable holds the contextpath of the application
     * @param nodeList    List of space/module
     * @param mapper      For creating ObjectNode
     * @param nodeArray   Creating Array of ObjectNode for nodes in spaceoverview
     *                    graph
     * @param allNodeList List of all the nodes of spaceoverview graph
     * @param isModule    boolean variable to indicate whether it is module or not
     */
    private void createNodeForOverviewGraph(String contextPath, Iterable<? extends IVSpaceElement> nodeList,
            ObjectMapper mapper, ArrayNode nodeArray, List<String> allNodeList) {

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
                    allNodeList.add(id);
                }
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
    private void createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space,
            ObjectNode node, String contextPath, String spaceId) {
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
    private void createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, ObjectNode node,
            String contextPath, String moduleId) {
        linkPathBuilder.setLength(0);
        linkPathBuilder.append(contextPath);
        linkPathBuilder.append(ModuleController.STAFF_MODULE_PATH);
        linkPathBuilder.append(moduleId);
        node.put("link", linkPathBuilder.toString());
        node.put("img", "");
        node.put("isModule", true);
    }

    /**
     * creating json for edge between nodes in the spaceoverview graph
     * 
     * @param mapper       For creating ObjectNode
     * @param linkArray    Creating Array of ObjectNode for edge between nodes in
     *                     the spaceoverview graph
     * @param allNodeList  List of all the nodes of spaceoverview graph
     * @param spaceLinkMap List of all links corresponding to each space
     */
    private void createEdgeForOverviewGraph(ObjectMapper mapper, ArrayNode linkArray, List<String> allNodeList,
            Map<String, List<String>> spaceLinkMap) {

        for (Entry<String, List<String>> entry : spaceLinkMap.entrySet()) {
            for (String value : entry.getValue()) {
                ObjectNode link = mapper.createObjectNode();
                link.put("target", allNodeList.indexOf(value));
                link.put("source", allNodeList.indexOf(entry.getKey()));
                linkArray.add(link);
            }
        }
    }
}
