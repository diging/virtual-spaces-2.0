package edu.asu.diging.vspace.web.staff;

import java.util.ArrayList;
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

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.impl.SpaceOverviewManager;

/**
 * 
 * @author Avirup Biswas
 *
 */
@Controller
public class SpaceOverviewController {

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private SpaceOverviewManager spaceOverviewManager;

    @RequestMapping("/staff/overview")
    public String spaceOverview(HttpServletRequest request, Model model) throws JsonProcessingException {

        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();

        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = spaceOverviewManager.getSpaceToModuleLinks();

        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = spaceOverviewManager.getSpaceToSpaceLinks();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode nodeArray = mapper.createArrayNode();
        ArrayNode linkArray = mapper.createArrayNode();

        spaceOverviewManager.getSpaceLinkMap(request.getContextPath(), mapper, nodeArray, spaceLinkMap,
                spaceToModuleLinksMap, spaceToSpaceLinksMap);

        spaceOverviewManager.createModuleNodeInOverviewGraph(request.getContextPath(), mapper, nodeArray);

        spaceOverviewManager.createLinkForOverviewGraph(mapper, linkArray, spaceLinkMap);

        model.addAttribute("overviewNode", mapper.writeValueAsString(nodeArray));
        model.addAttribute("overviewLink", mapper.writeValueAsString(linkArray));
        return "staff/spaces/graph";
    }
}
