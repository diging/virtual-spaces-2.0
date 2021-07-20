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
import edu.asu.diging.vspace.core.services.impl.SpaceOverviewDataFormat;
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
    private SpaceOverviewDataFormat spaceOverviewDataFormat;

    @RequestMapping("/staff/overview")
    public String spaceOverview(HttpServletRequest request, Model model) throws JsonProcessingException {

        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = spaceOverviewManager.getSpaceToModuleLinks();

        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = spaceOverviewManager.getSpaceToSpaceLinks();

        Map<String, List<String>> spaceLinkMap = spaceOverviewManager.getSpaceLinkMap(spaceToModuleLinksMap,
                spaceToSpaceLinksMap);

        String nodeJson = spaceOverviewDataFormat.createNodeForOverviewGraph(request.getContextPath());

        String linkJson = spaceOverviewDataFormat.createLinkForOverviewGraph(spaceLinkMap);

        model.addAttribute("overviewNode", nodeJson);
        model.addAttribute("overviewLink", linkJson);

        return "staff/spaces/graph";
    }
}
