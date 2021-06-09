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
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
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
        Iterable<Module> allModuleList = moduleRepo.findAll();

        List<String> allNodeList = new ArrayList<>();
        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();

        Iterable<ModuleLinkDisplay> spaceToModuleLinks = moduleLinkDisplayRepository.findAll();
        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = new HashMap<>();
        getSpaceToAllOtherlink(spaceToModuleLinks, spaceToModuleLinksMap, true);

        Iterable<SpaceLinkDisplay> spaceToSpaceLinksList = spaceLinkDisplayRepository.findAll();
        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = new HashMap<>();
        getSpaceToAllOtherlink(spaceToSpaceLinksList, spaceToSpaceLinksMap, false);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();

        getSpaceLinkMap(request.getContextPath(), allSpaceList, mapper, nodeArray, allNodeList, spaceLinkMap,
                spaceToModuleLinksMap, spaceToSpaceLinksMap);

        if (allModuleList != null) {
            nodeFormation(request.getContextPath(), allModuleList, mapper, nodeArray, allNodeList, true);
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
     * @param allSpaceList
     * @param mapper
     * @param nodeArray
     * @param allNodeList
     * @param spaceLinkMap
     * @param allSpaceToModuleLinksMap
     * @param allSpaceToSpaceLinksMap
     */
    private void getSpaceLinkMap(String contextPath, Iterable<Space> allSpaceList, ObjectMapper mapper,
            ArrayNode nodeArray, List<String> allNodeList, Map<String, List<String>> spaceLinkMap,
            Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap) {
        if (allSpaceList != null) {
            nodeFormation(contextPath, allSpaceList, mapper, nodeArray, allNodeList, false);
            for (Space space : allSpaceList) {
                List<ModuleLinkDisplay> spaceToModulelinksList = spaceToModuleLinksMap.get(space.getId());
                List<SpaceLinkDisplay> spaceLinks = spaceToSpaceLinksMap.get(space.getId());
                List<SpaceLinkDisplay> spaceToSpaceLinksList = new ArrayList<>();
                if (spaceLinks != null) {
                    spaceToSpaceLinksList.addAll(spaceLinks);
                }
                List<String> spaceAllLink = new ArrayList<>();
                if(spaceToModulelinksList!=null) {
                    spaceToModulelinksList.forEach(link -> {
                        if(link.getLink()!=null && link.getLink().getModule()!=null)
                            spaceAllLink.add(link.getLink().getModule().getId());
                    });
                }

                if(spaceToSpaceLinksList!=null) {
                    spaceToSpaceLinksList.forEach(link -> {
                        if(link.getLink()!=null && link.getLink().getTarget()!=null)
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
    private <T> void getSpaceToAllOtherlink(Iterable<T> spaceToAllOtherLinksList,
            Map<String, List<T>> spaceToAllOtherLinksMap, boolean isModule) {

        spaceToAllOtherLinksList.forEach(link -> {
            String spaceId = isModule == true ? ((IModuleLinkDisplay) link).getLink().getSpace().getId()
                    : ((ISpaceLinkDisplay) link).getLink().getSourceSpace().getId();
            if (!spaceToAllOtherLinksMap.containsKey(spaceId)) {
                spaceToAllOtherLinksMap.put(spaceId, new ArrayList<>());
            }
            spaceToAllOtherLinksMap.get(spaceId).add(link);
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
    private <T> void nodeFormation(String contextPath, Iterable<T> nodeList, ObjectMapper mapper, ArrayNode nodeArray,
            List<String> allNodeList, boolean isModule) {

        for (T nodeval : nodeList) {
            VSpaceElement nodeObj = isModule == true ? (Module) nodeval : (Space) nodeval;
            ObjectNode node = mapper.createObjectNode();
            node.put("name", nodeObj.getName());
            StringBuilder linkPathBuilder = new StringBuilder();
            linkPathBuilder.append(contextPath);
            String path = isModule == true ? ModuleController.STAFF_MODULE_PATH : SpaceController.STAFF_SPACE_PATH;
            linkPathBuilder.append(path);
            linkPathBuilder.append(nodeObj.getId());
            node.put("link", linkPathBuilder.toString());
            String imagePath = "";
            if (!isModule) {
                StringBuilder imagePathBuilder = new StringBuilder();
                imagePathBuilder.append(contextPath);
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
        }
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

        for (Entry<String, List<String>> entry : spaceLinkMap.entrySet()) {
            for (String value : entry.getValue()) {
                ObjectNode node = mapper.createObjectNode();
                node.put("target", allNodeList.indexOf(value));
                node.put("source", allNodeList.indexOf(entry.getKey()));
                linkArray.add(node);
            }
        }
    }
}
