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

    void getSpaceLinkMap(String contextPath, ObjectMapper mapper, ArrayNode nodeArray,
            Map<String, List<String>> spaceLinkMap, Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap);

    void createNodeForOverviewGraph(String contextPath, Iterable<? extends IVSpaceElement> nodeList,
            ObjectMapper mapper, ArrayNode nodeArray);

    void createModuleNodeInOverviewGraph(String contextPath, ObjectMapper mapper, ArrayNode nodeArray);

    void createSpaceNodeInOverviewGraph(String contextPath, ObjectMapper mapper, ArrayNode nodeArray);

    void createLinkForOverviewGraph(ObjectMapper mapper, ArrayNode linkArray, Map<String, List<String>> spaceLinkMap);

    void createSpaceNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, Space space, ObjectNode node,
            String contextPath, String spaceId);

    void createModuleNode(StringBuilder linkPathBuilder, StringBuilder imagePathBuilder, ObjectNode node,
            String contextPath, String moduleId);

}
