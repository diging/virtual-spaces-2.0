package edu.asu.diging.vspace.core.services;

import java.util.List;
import java.util.Map;

import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

public interface ISpaceOverviewManager {

    Map<String, List<ModuleLinkDisplay>> getSpaceToModuleLinks();

    Map<String, List<SpaceLinkDisplay>> getSpaceToSpaceLinks();

    Map<String, List<String>> getSpaceLinkedToSpacesAndModules(Map<String, List<ModuleLinkDisplay>> spaceToModuleLinksMap,
            Map<String, List<SpaceLinkDisplay>> spaceToSpaceLinksMap);

}
