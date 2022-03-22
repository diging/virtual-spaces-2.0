package edu.asu.diging.vspace.core.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

public interface ISpaceOverviewManager {

    Map<String, Set<ModuleLinkDisplay>> getSpaceToModuleLinks();

    Map<String, Set<SpaceLinkDisplay>> getSpaceToSpaceLinks();

    Map<String, List<String>> getSpacesToSpacesAndModulesMap(Map<String, Set<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, Set<SpaceLinkDisplay>> spaceToSpaceLinksMap);

}
