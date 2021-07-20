package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.services.ISpaceOverviewManager;

@Service
public class SpaceOverviewManager implements ISpaceOverviewManager {

    @Autowired
    private SpaceRepository spaceRepo;

    @Autowired
    private SpaceLinkDisplayRepository spaceLinkDisplayRepository;

    @Autowired
    private ModuleLinkDisplayRepository moduleLinkDisplayRepository;

    /**
     * This method is used to fetch all linked modules corresponding to a space.
     */
    @Override
    public Map<String, List<ModuleLinkDisplay>> getSpaceToModuleLinks() {

        Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap = new HashMap<>();
        Iterable<ModuleLinkDisplay> spaceToModuleLinksList = moduleLinkDisplayRepository.findAll();

        for (ModuleLinkDisplay link : spaceToModuleLinksList) {
            String spaceId = null;

            if (link.getLink() != null && link.getLink().getSpace() != null) {
                spaceId = link.getLink().getSpace().getId();
                if (!spaceToModuleLinksMap.containsKey(spaceId)) {
                    spaceToModuleLinksMap.put(spaceId, new ArrayList<>());
                }
                spaceToModuleLinksMap.get(spaceId).add(link);
            }
        }
        return spaceToModuleLinksMap;
    }

    /**
     * This method is used to fetch all linked space corresponding to a space
     */
    @Override
    public Map<String, List<SpaceLinkDisplay>> getSpaceToSpaceLinks() {

        Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap = new HashMap<>();
        Iterable<SpaceLinkDisplay> spaceToSpaceLinksList = spaceLinkDisplayRepository.findAll();
        for (SpaceLinkDisplay link : spaceToSpaceLinksList) {
            String spaceId = null;
            if (link.getLink() != null && link.getLink().getSourceSpace() != null) {

                spaceId = link.getLink().getSourceSpace().getId();
                if (!spaceToSpaceLinksMap.containsKey(spaceId)) {
                    spaceToSpaceLinksMap.put(spaceId, new ArrayList<>());
                }
                spaceToSpaceLinksMap.get(spaceId).add(link);
            }
        }
        return spaceToSpaceLinksMap;
    }

    /**
     * To retrieve all space to space and space to module links corresponding to
     * each space
     * 
     * @param spaceLinkMap          For holding list of all links corresponding to
     *                              each space
     * @param spaceToModuleLinksMap For getting list of links from one space to
     *                              other modules
     * @param spaceToSpaceLinksMap  For getting list of links from one space to
     *                              other spaces
     */
    @Override
    public Map<String, List<String>> getSpaceLinkMap(Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap) {

        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        if (allSpacesList != null) {
            for (Space space : allSpacesList) {
                List<ModuleLinkDisplay> spaceToModulelinksList = spaceToModuleLinksMap.get(space.getId());
                List<SpaceLinkDisplay> spaceToSpaceLinksList = spaceToSpaceLinksMap.get(space.getId());
                List<String> listOfSpaceIdAndModuleId = new ArrayList<>();

                if (spaceToModulelinksList != null) {
                    spaceToModulelinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getModule() != null) {
                            listOfSpaceIdAndModuleId.add(link.getLink().getModule().getId());
                        }
                    });
                }
                if (spaceToSpaceLinksList != null) {
                    spaceToSpaceLinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getTarget() != null) {
                            listOfSpaceIdAndModuleId.add(link.getLink().getTarget().getId());
                        }
                    });
                }
                spaceLinkMap.put(space.getId(), listOfSpaceIdAndModuleId);
            }
        }
        return spaceLinkMap;
    }
}
