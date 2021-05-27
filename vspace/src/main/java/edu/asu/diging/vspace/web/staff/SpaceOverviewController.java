package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

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
    public String displayDashboard(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<Space> spaceList = spaceRepo.findAllByOrderByCreationDateAsc();
        List<Module> moduleList = moduleRepo.findAllByOrderByCreationDateAsc();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();
        List<String> allNodeList = new ArrayList<>();
        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();

        List<IModuleLinkDisplay> AllSpaceToModulelinksList = moduleLinkDisplayRepository
                .findModuleLinkDisplaysForAllSpace();
        Map<String, List<IModuleLinkDisplay>> AllSpaceToModulelinksListMap = new HashMap<>();

        AllSpaceToModulelinksList.forEach(link -> {
            if (!AllSpaceToModulelinksListMap.containsKey(link.getLink().getSpace().getId())) {
                List<IModuleLinkDisplay> tempSpaceToModulelinkList = new ArrayList<>();
                tempSpaceToModulelinkList.add(link);
                AllSpaceToModulelinksListMap.put(link.getLink().getSpace().getId(), tempSpaceToModulelinkList);
            } else {
                List<IModuleLinkDisplay> tempSpaceToModulelinkList = AllSpaceToModulelinksListMap
                        .get(link.getLink().getSpace().getId());
                tempSpaceToModulelinkList.add(link);
                AllSpaceToModulelinksListMap.put(link.getLink().getSpace().getId(), tempSpaceToModulelinkList);
            }
        });

        List<ISpaceLinkDisplay> AllSpaceToSpacelinksList = spaceLinkDisplayRepository
                .findAllSpaceLinkDisplaysForSpace();
        Map<String, List<ISpaceLinkDisplay>> AllSpaceToSpacelinksListMap = new HashMap<>();

        AllSpaceToSpacelinksList.forEach(link -> {
            if (!AllSpaceToSpacelinksListMap.containsKey(link.getLink().getSourceSpace().getId())) {
                List<ISpaceLinkDisplay> tempSpaceToSpacelinkList = new ArrayList<>();
                tempSpaceToSpacelinkList.add(link);
                AllSpaceToSpacelinksListMap.put(link.getLink().getSourceSpace().getId(), tempSpaceToSpacelinkList);
            } else {
                List<ISpaceLinkDisplay> tempSpaceToSpacelinkList = AllSpaceToSpacelinksListMap
                        .get(link.getLink().getSourceSpace().getId());
                tempSpaceToSpacelinkList.add(link);
                AllSpaceToSpacelinksListMap.put(link.getLink().getSourceSpace().getId(), tempSpaceToSpacelinkList);
            }
        });

        List<SpaceLinkDisplay> AllSpaceLinkForGivenOrNullSpaceStatusList = spaceLinkDisplayRepository
                .findAllSpaceLinksForGivenOrNullSpaceStatus(SpaceStatus.PUBLISHED);
        Map<String, List<ISpaceLinkDisplay>> AllSpaceLinkForGivenOrNullSpaceStatustMap = new HashMap<>();

        AllSpaceLinkForGivenOrNullSpaceStatusList.forEach(link -> {
            if (!AllSpaceLinkForGivenOrNullSpaceStatustMap.containsKey(link.getLink().getSourceSpace().getId())) {
                List<ISpaceLinkDisplay> tempSpaceToSpacelinkList = new ArrayList<>();
                tempSpaceToSpacelinkList.add(link);
                AllSpaceLinkForGivenOrNullSpaceStatustMap.put(link.getLink().getSourceSpace().getId(),
                        tempSpaceToSpacelinkList);
            } else {
                List<ISpaceLinkDisplay> tempSpaceToSpacelinkList = AllSpaceLinkForGivenOrNullSpaceStatustMap
                        .get(link.getLink().getSourceSpace().getId());
                tempSpaceToSpacelinkList.add(link);
                AllSpaceLinkForGivenOrNullSpaceStatustMap.put(link.getLink().getSourceSpace().getId(),
                        tempSpaceToSpacelinkList);
            }
        });
        if (spaceList != null && !spaceList.isEmpty()) {
            spaceList.forEach(s -> {
                ObjectNode node = mapper.createObjectNode();
                node.put("name", s.getName());
                node.put("link", request.getContextPath() + "/staff/space/" + s.getId());
                node.put("img", request.getContextPath() + "/api/image/" + s.getImage().getId());
                node.put("isModule", false);
                List<String> spaceAllLink = new ArrayList<>();
                List<IModuleLinkDisplay> spaceToModulelinksList = AllSpaceToModulelinksListMap.get(s.getId());
                List<ISpaceLinkDisplay> spaceLinks = null;
                if (s.isShowUnpublishedLinks()) {
                    spaceLinks = AllSpaceToSpacelinksListMap.get(s.getId());
                } else {
                    spaceLinks = AllSpaceLinkForGivenOrNullSpaceStatustMap.get(s.getId());
                }
                List<ISpaceLinkDisplay> spaceToSpaceLinksList = null;
                if (spaceLinks != null) {
                    spaceToSpaceLinksList = spaceLinks.stream().filter(spaceLinkDisplayObj -> !spaceLinkDisplayObj
                            .getLink().getTargetSpace().isHideIncomingLinks()).collect(Collectors.toList());
                }
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
                spaceLinkMap.put(s.getId(), spaceAllLink);
                allNodeList.add(s.getId());
                nodeArray.add(node);
            });
        }
        if (moduleList != null && !moduleList.isEmpty()) {
            moduleList.forEach(m -> {
                ObjectNode node = mapper.createObjectNode();
                node.put("name", m.getName());
                node.put("link", request.getContextPath() + "/staff/module/" + m.getId());
                node.put("img", "");
                node.put("isModule", true);
                allNodeList.add(m.getId());
                nodeArray.add(node);
            });
        }
        model.addAttribute("overViewNode", mapper.writeValueAsString(nodeArray));
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
        model.addAttribute("overViewLink", mapper.writeValueAsString(linkArray));
        return "staff/spaces/graph";
    }
}
