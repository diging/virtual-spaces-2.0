package edu.asu.diging.vspace.core.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * This class is used to get the space overview graph in desired data format.
 * 
 * @author abiswa15
 *
 */

@Component
public class SpaceOverviewDataFormatter {

    @Autowired
    private SpaceOverviewJsonFormat spaceOverviewJsonFormat;

    /**
     * * This method is used to get the space overview graph in JSON format.
     * 
     * @param contextPath                   The context path of the application.
     * @param spacesToToSpacesAndModulesMap The map which contains mapping between a
     *                                      space to all linked spaces and modules.
     * 
     */
    public Map<String, String> getJsonFormat(String contextPath,
            Map<String, List<String>> spacesToToSpacesAndModulesMap) throws JsonProcessingException {

        String nodesJson = spaceOverviewJsonFormat.createNodes(contextPath,spacesToToSpacesAndModulesMap);
        Map<String, String> jsonFormatMap = new HashMap<>();
        jsonFormatMap.put("nodesJson", nodesJson);
        return jsonFormatMap;
    }
}
