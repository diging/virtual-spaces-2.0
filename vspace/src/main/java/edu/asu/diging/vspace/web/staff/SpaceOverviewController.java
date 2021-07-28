package edu.asu.diging.vspace.web.staff;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.services.impl.SpaceOverviewFormatter;
import edu.asu.diging.vspace.core.services.impl.SpaceOverviewManager;

/**
 * 
 * @author Avirup Biswas
 *
 */
@Controller
public class SpaceOverviewController {

    @Autowired
    private SpaceOverviewManager spaceOverviewManager;

    @Autowired
    private SpaceOverviewFormatter spaceOverviewDataFormatter;

    @RequestMapping("/staff/overview")
    public String spaceOverview(HttpServletRequest request, Model model) throws JsonProcessingException {

        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = spaceOverviewManager.getSpaceToModuleLinks();

        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = spaceOverviewManager.getSpaceToSpaceLinks();

        Map<String, List<String>> spacesToSpacesAndModulesMap = spaceOverviewManager.getSpacesToSpacesAndModulesMap(spaceToModuleLinksMap,
                spaceToSpaceLinksMap);

        String nodesJson = spaceOverviewDataFormatter.createNodes(request.getContextPath());

        String linksJson = spaceOverviewDataFormatter.createEdges(spacesToSpacesAndModulesMap);

        model.addAttribute("overviewNode", nodesJson);
        model.addAttribute("overviewLink", linksJson);

        return "staff/spaces/graph";
    }
}
