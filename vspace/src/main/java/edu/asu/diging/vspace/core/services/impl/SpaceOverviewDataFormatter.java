package edu.asu.diging.vspace.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class SpaceOverviewDataFormatter {

    @Autowired
    private SpaceOverviewJsonFormat spaceOverviewJsonFormat;

    public Map<String, String> getJsonFormat(String contextPath,
            Map<String, List<String>> spacesToToSpacesAndModulesMap) throws JsonProcessingException {

        String nodesJson = spaceOverviewJsonFormat.createNodes(contextPath);
        String linksJson = spaceOverviewJsonFormat.createEdges(spacesToToSpacesAndModulesMap);

        Map<String, String> jsonFormatMap = new HashMap<>();
        jsonFormatMap.put("nodesJson", nodesJson);
        jsonFormatMap.put("linksJson", linksJson);

        return jsonFormatMap;
    }
}
