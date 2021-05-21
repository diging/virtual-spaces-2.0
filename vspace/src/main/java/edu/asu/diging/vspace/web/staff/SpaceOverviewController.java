package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
import java.util.HashMap;
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

import edu.asu.diging.vspace.core.data.ExternalLinkRepository;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.display.IExternalLinkDisplay;
import edu.asu.diging.vspace.core.model.display.IModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.ExternalLink;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.services.IExternalLinkManager;
import edu.asu.diging.vspace.core.services.IModuleLinkManager;
import edu.asu.diging.vspace.core.services.ISpaceLinkManager;
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
    private ExternalLinkRepository externalRepo;

    @Autowired
    private IModuleLinkManager moduleLinkManager;

    @Autowired
    private ISpaceLinkManager spaceLinkManager;

    @Autowired
    private IExternalLinkManager externalLinkManager;

    @RequestMapping("/staff/overview")
    public String displayDashboard(HttpServletRequest request, Model model) throws JsonProcessingException {
        List<Space> spaceList = spaceRepo.findAllByOrderByCreationDateAsc();
        List<Module> moduleList = moduleRepo.findAllByOrderByCreationDateAsc();
        List<ExternalLink> externalList = (List<ExternalLink>) externalRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();
        List<String> allNodeList = new ArrayList<>();
        Map<String, List<String>> spaceLinkMap = new HashMap<>();
        spaceList.forEach(s -> {
            ObjectNode node = mapper.createObjectNode();
            node.put("name", s.getName());
            node.put("link", request.getContextPath() + "/staff/space/" + s.getId());
            node.put("img", request.getContextPath() + "/api/image/" + s.getImage().getId());
            List<String> spaceAllLink = new ArrayList<>();
            List<IModuleLinkDisplay> spaceToModulelinksList = moduleLinkManager.getLinkDisplays(s.getId());
            List<ISpaceLinkDisplay> spaceLinks;
            if (s.isShowUnpublishedLinks()) {
                spaceLinks = spaceLinkManager.getLinkDisplays(s.getId());
            } else {
                spaceLinks = spaceLinkManager.getSpaceLinkForGivenOrNullSpaceStatus(s.getId(), SpaceStatus.PUBLISHED);
            }
            List<ISpaceLinkDisplay> spaceToSpaceLinksList = spaceLinks.stream().filter(
                    spaceLinkDisplayObj -> !spaceLinkDisplayObj.getLink().getTargetSpace().isHideIncomingLinks())
                    .collect(Collectors.toList());

            List<IExternalLinkDisplay> spaceToExternalLinksList = externalLinkManager.getLinkDisplays(s.getId());

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
            if (spaceToExternalLinksList != null && !spaceToExternalLinksList.isEmpty()) {
                
                spaceToExternalLinksList.forEach(link -> {
                    spaceAllLink.add(link.getExternalLink().getId());
                });
            }
            spaceLinkMap.put(s.getId(), spaceAllLink);
            allNodeList.add(s.getId());
            nodeArray.add(node);
        });

        moduleList.forEach(m -> {
            ObjectNode node = mapper.createObjectNode();
            node.put("name", m.getName());
            node.put("link", request.getContextPath() + "/staff/module/" + m.getId());
            node.put("img", "");
            allNodeList.add(m.getId());
            nodeArray.add(node);
        });

        externalList.forEach(e -> {
            ObjectNode node = mapper.createObjectNode();
            node.put("name", e.getName());
            node.put("link", e.getExternalLink());
            node.put("img", "");
            allNodeList.add(e.getId());
            nodeArray.add(node);
        });

        model.addAttribute("overViewNode", mapper.writeValueAsString(nodeArray));
        spaceLinkMap.forEach((k,v)->{
            if(!v.isEmpty())
            {
                v.forEach(e -> {
                    ObjectNode node = mapper.createObjectNode();
                    node.put("target", allNodeList.indexOf(e));
                    node.put("source", allNodeList.indexOf(k));
                    linkArray.add(node);
                });
            }
        });
        System.out.print("Value of the Json :");
        System.out.print(mapper.writeValueAsString(linkArray));
        
        model.addAttribute("overViewLink", mapper.writeValueAsString(linkArray));
        
        return "staff/spaces/graph";
    }
}
