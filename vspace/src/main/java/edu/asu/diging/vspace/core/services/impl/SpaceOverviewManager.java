package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * This method is used to fetch all moduleLinks which are connected to a space.
     * 
     * @return a Map whose key is spaceId and value is list of moduleLinks connected
     *         with the spaceId.
     */
    @Override
    public Map<String, Set<ModuleLinkDisplay>> getSpaceToModuleLinks() {

        Map<String, Set<ModuleLinkDisplay>> spaceToModuleLinksMap = new HashMap<>();
        Iterable<ModuleLinkDisplay> spaceToModuleLinksList = moduleLinkDisplayRepository.findAll();

        for (ModuleLinkDisplay link : spaceToModuleLinksList) {
            if (link.getLink() != null && link.getLink().getSpace() != null) {
                String spaceId = link.getLink().getSpace().getId();
                if (!spaceToModuleLinksMap.containsKey(spaceId)) {
                    spaceToModuleLinksMap.put(spaceId, new HashSet<>());
                }
                spaceToModuleLinksMap.get(spaceId).add(link);
            }
        }
        return spaceToModuleLinksMap;
    }

    /**
     * This method is used to fetch all linked spacelinks corresponding to a
     * space(i.e. a space connected to other spaces)
     * 
     * @return a Map whose key is spaceId and value is list of spaceLinks connected
     *         with the spaceId.
     */
    @Override
    public Map<String, Set<SpaceLinkDisplay>> getSpaceToSpaceLinks() {

        Map<String, Set<SpaceLinkDisplay>> spaceToSpaceLinksMap = new HashMap<>();
        Iterable<SpaceLinkDisplay> spaceToSpaceLinksList = spaceLinkDisplayRepository.findAll();
        for (SpaceLinkDisplay link : spaceToSpaceLinksList) {
            if (link.getLink() != null && link.getLink().getSourceSpace() != null) {
                String targerSpaceId = null;
                if (link.getLink().getTargetSpace() != null) {
                    targerSpaceId = link.getLink().getTargetSpace().getId();
                }
                String spaceId = link.getLink().getSourceSpace().getId();
                if (targerSpaceId != spaceId) {
                    if (!spaceToSpaceLinksMap.containsKey(spaceId)) {
                        spaceToSpaceLinksMap.put(spaceId, new HashSet<>());
                    }
                    spaceToSpaceLinksMap.get(spaceId).add(link);
                }
            }
        }
        return spaceToSpaceLinksMap;
    }

    /**
     * To retrieve all space to space and space to module links corresponding to
     * each space(i.e. a space connected with all other spaces and modules)
     * 
     * @param spaceLinkMap          For holding list of all links corresponding to
     *                              each space
     * @param spaceToModuleLinksMap For getting list of links from one space to
     *                              other modules
     * @param spaceToSpaceLinksMap  For getting list of links from one space to
     *                              other spaces
     */
    @Override
    public Map<String, List<String>> getSpacesToSpacesAndModulesMap(
            Map<String, Set<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, Set<SpaceLinkDisplay>> spaceToSpaceLinksMap) {

        Map<String, List<String>> spaceLinkMap = new LinkedHashMap<>();
        Iterable<Space> allSpacesList = spaceRepo.findAll();
        if (allSpacesList != null) {
            for (Space space : allSpacesList) {
                Set<ModuleLinkDisplay> spaceToModulelinksList = spaceToModuleLinksMap.get(space.getId());
                Set<SpaceLinkDisplay> spaceToSpaceLinksList = spaceToSpaceLinksMap.get(space.getId());
                List<String> listOfSpaceIdAndModuleIds = new ArrayList<>();

                if (spaceToModulelinksList != null) {
                    spaceToModulelinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getModule() != null) {
                            listOfSpaceIdAndModuleIds.add(link.getLink().getModule().getId());
                        }
                    });
                }
                if (spaceToSpaceLinksList != null) {
                    spaceToSpaceLinksList.forEach(link -> {
                        if (link.getLink() != null && link.getLink().getTarget() != null) {
                            listOfSpaceIdAndModuleIds.add(link.getLink().getTarget().getId());
                        }
                    });
                }
                spaceLinkMap.put(space.getId(), listOfSpaceIdAndModuleIds);
            }
        }
        return spaceLinkMap;
    }
}
