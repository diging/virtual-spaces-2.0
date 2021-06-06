package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import edu.asu.diging.vspace.core.model.display.ILinkDisplay;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSpaceElement;
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
        Iterable<Space> allSpaceList = spaceRepo.findAll();
        List<Space> spaceList = new ArrayList<>();
        allSpaceList.forEach(space -> spaceList.add(space));
        Iterable<Module> allModuleList = moduleRepo.findAll();
        List<Module> moduleList = new ArrayList<>();
        allModuleList.forEach(module -> moduleList.add(module));
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();
        List<String> allNodeList = new ArrayList<>();
        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();

        List<IModuleLinkDisplay> allSpaceToModulelinksList = moduleLinkDisplayRepository
                .findModuleLinkDisplaysForAllSpace();

        Map<String, List<IModuleLinkDisplay>> allSpaceToModulelinksListMap = new HashMap<>();

        getSpaceToAllOtherlink(allSpaceToModulelinksList, allSpaceToModulelinksListMap, true);

        Iterable<SpaceLinkDisplay> spaceToSpacelinksList = spaceLinkDisplayRepository.findAll();

        List<ISpaceLinkDisplay> allSpaceToSpacelinksList = new ArrayList<>();

        spaceToSpacelinksList.forEach(link -> allSpaceToSpacelinksList.add(link));

        Map<String, List<ISpaceLinkDisplay>> allSpaceToSpacelinksListMap = new HashMap<>();

        getSpaceToAllOtherlink(allSpaceToSpacelinksList, allSpaceToSpacelinksListMap, false);

        getSpaceLinkMap(request, spaceList, mapper, nodeArray, allNodeList, spaceLinkMap, allSpaceToModulelinksListMap,
                allSpaceToSpacelinksListMap);

        if (moduleList != null && !moduleList.isEmpty()) {
            nodeFormation(request, moduleList, mapper, nodeArray, allNodeList, true);
        }
        sourceToTargetFormation(mapper, linkArray, allNodeList, spaceLinkMap);
        model.addAttribute("overviewNode", mapper.writeValueAsString(nodeArray));
        model.addAttribute("overviewLink", mapper.writeValueAsString(linkArray));
        return "staff/spaces/graph";
    }

    /**
     * To retrieve all space to space and space to module links corresponding to
     * each space
     * 
     * @param request
     * @param spaceList
     * @param mapper
     * @param nodeArray
     * @param allNodeList
     * @param spaceLinkMap
     * @param allSpaceToModulelinksListMap
     * @param allSpaceToSpacelinksListMap
     */
    private void getSpaceLinkMap(HttpServletRequest request, List<Space> spaceList, ObjectMapper mapper,
            ArrayNode nodeArray, List<String> allNodeList, Map<String, List<String>> spaceLinkMap,
            Map<String, List<IModuleLinkDisplay>> allSpaceToModulelinksListMap,
            Map<String, List<ISpaceLinkDisplay>> allSpaceToSpacelinksListMap) {
        if (spaceList != null && !spaceList.isEmpty()) {
            nodeFormation(request, spaceList, mapper, nodeArray, allNodeList, false);
            for (Space space : spaceList) {
                List<IModuleLinkDisplay> spaceToModulelinksList = allSpaceToModulelinksListMap.get(space.getId());
                List<ISpaceLinkDisplay> spaceLinks = null;
                spaceLinks = allSpaceToSpacelinksListMap.get(space.getId());
                List<ISpaceLinkDisplay> spaceToSpaceLinksList = null;
                if (spaceLinks != null) {
                    spaceToSpaceLinksList = new ArrayList<>();
                    spaceToSpaceLinksList.addAll(spaceLinks);
                }
                List<String> spaceAllLink = new ArrayList<>();
                if (spaceToModulelinksList != null && !spaceToModulelinksList.isEmpty()) {

                    spaceToModulelinksList.forEach(link -> {
                        spaceAllLink.add(link.getLink().getModule().getId());
                    });
                }
                if (spaceToSpaceLinksList != null && !spaceToSpaceLinksList.isEmpty()) {

                    spaceToSpaceLinksList.forEach(link -> {
                        spaceAllLink.add(link.getLink().getTarget().getId());
                    });
                }
                spaceLinkMap.put(space.getId(), spaceAllLink);
            }
        }
    }

    /**
     * To map each space to all linked space or each space to all linked module
     * 
     * @param <T>
     * @param spaceToAllOtherlinksList
     * @param spaceToAllOtherlinksMap
     * @param isModule
     */
    private <T> void getSpaceToAllOtherlink(List<T> spaceToAllOtherlinksList,
            Map<String, List<T>> spaceToAllOtherlinksMap, boolean isModule) {
        spaceToAllOtherlinksList.forEach(link -> {
            String spaceId = isModule == true ? ((IModuleLinkDisplay) link).getLink().getSpace().getId()
                    : ((ISpaceLinkDisplay) link).getLink().getSourceSpace().getId();
            if (!spaceToAllOtherlinksMap.containsKey(spaceId)) {
                List<T> tempSpaceToOtherlinkList = new ArrayList<>();
                spaceToAllOtherlinksMap.put(spaceId, tempSpaceToOtherlinkList);
            }
            List<T> tempSpaceToOtherlinkList = spaceToAllOtherlinksMap.get(spaceId);
            tempSpaceToOtherlinkList.add(link);
        });
    }

    /**
     * creating json for space and module node in the spaceoverview graph
     * 
     * @param <T>
     * @param request
     * @param nodeList
     * @param mapper
     * @param nodeArray
     * @param allNodeList
     * @param isModule
     */
    private <T> void nodeFormation(HttpServletRequest request, List<T> nodeList, ObjectMapper mapper,
            ArrayNode nodeArray, List<String> allNodeList, boolean isModule) {
        nodeList.forEach(nodeval -> {
            VSpaceElement nodeObj = isModule == true ? (Module) nodeval : (Space) nodeval;
            ObjectNode node = mapper.createObjectNode();
            node.put("name", nodeObj.getName());
            StringBuilder linkPathBuilder = new StringBuilder();
            linkPathBuilder.append(request.getContextPath());
            String path = isModule == true ? ModuleController.STAFF_MODULE_PATH : SpaceController.STAFF_SPACE_PATH;
            linkPathBuilder.append(path);
            linkPathBuilder.append(nodeObj.getId());
            node.put("link", linkPathBuilder.toString());
            String imagePath = "";
            if (!isModule) {
                StringBuilder imagePathBuilder = new StringBuilder();
                imagePathBuilder.append(request.getContextPath());
                imagePathBuilder.append(ImageApiController.API_IMAGE_PATH);
                imagePathBuilder.append(((Space) nodeObj).getImage().getId());
                imagePath = imagePathBuilder.toString();
            }
            node.put("img", imagePath);
            node.put("isModule", isModule);
            if (!isModule) {
                if (((Space) nodeObj).getSpaceStatus() != null
                        && ((Space) nodeObj).getSpaceStatus().equals(SpaceStatus.UNPUBLISHED)) {
                    node.put("isUnpublished", true);
                } else {
                    node.put("isUnpublished", false);
                }
                if (((Space) nodeObj).isHideIncomingLinks()) {
                    node.put("isHideIncomingLinks", true);
                } else {
                    node.put("isHideIncomingLinks", false);
                }
            }
            nodeArray.add(node);
            allNodeList.add(nodeObj.getId());
        });
    }

    /**
     * creating json for source node to all target nodes in the spaceoverview graph
     * 
     * @param mapper
     * @param linkArray
     * @param allNodeList
     * @param spaceLinkMap
     */
    private void sourceToTargetFormation(ObjectMapper mapper, ArrayNode linkArray, List<String> allNodeList,
            Map<String, List<String>> spaceLinkMap) {
        spaceLinkMap.forEach((k, v) -> {
            if (!v.isEmpty()) {
                v.forEach(e -> {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("target", allNodeList.indexOf(e));
                    node.put("source", allNodeList.indexOf(k));
                    linkArray.add(node);
                });
            }
        });
    }
}
