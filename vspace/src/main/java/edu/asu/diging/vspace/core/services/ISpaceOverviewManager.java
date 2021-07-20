package edu.asu.diging.vspace.core.services;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import edu.asu.diging.vspace.core.model.IVSpaceElement;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;

public interface ISpaceOverviewManager {

    Map<String, List<ModuleLinkDisplay>> getSpaceToModuleLinks();

    Map<String, List<SpaceLinkDisplay>> getSpaceToSpaceLinks();

    Map<String, List<String>> getSpaceLinkMap(Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap);

}
