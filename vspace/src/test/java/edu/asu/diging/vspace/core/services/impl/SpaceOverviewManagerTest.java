package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.ModuleLinkDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.model.display.impl.ModuleLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.ModuleLink;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

public class SpaceOverviewManagerTest {
    @Mock
    private ModuleLinkDisplayRepository moduleLinkDisplayRepository;

    @Mock
    private SpaceLinkDisplayRepository spaceLinkDisplayRepository;

    @Mock
    private SpaceRepository spaceRepo;

    @InjectMocks
    private SpaceOverviewManager managerToTest;

    private List<ModuleLinkDisplay> spaceToModuleLinksList;

    private List<SpaceLinkDisplay> spaceToSpaceLinksList;

    private List<Space> allSpacesList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Space space = new Space();
        space.setId("SPACE_01");

        ModuleLinkDisplay moduleLinkDisplay1 = new ModuleLinkDisplay();
        ModuleLink moduleLink1 = new ModuleLink();
        moduleLink1.setSpace(space);
        Module module1 = new Module();
        module1.setId("MODULE_01");
        moduleLink1.setModule(module1);
        moduleLinkDisplay1.setLink(moduleLink1);

        ModuleLinkDisplay moduleLinkDisplay2 = new ModuleLinkDisplay();
        ModuleLink moduleLink2 = new ModuleLink();
        moduleLink2.setSpace(space);
        Module module2 = new Module();
        module2.setId("MODULE_02");
        moduleLink2.setModule(module2);
        moduleLinkDisplay2.setLink(moduleLink2);

        spaceToModuleLinksList = new ArrayList<>();
        spaceToModuleLinksList.add(moduleLinkDisplay1);
        spaceToModuleLinksList.add(moduleLinkDisplay2);

        SpaceLinkDisplay spaceLinkDisplay1 = new SpaceLinkDisplay();
        SpaceLink spaceLink1 = new SpaceLink();
        spaceLink1.setSourceSpace(space);
        Space targetSpace1 = new Space();
        targetSpace1.setId("SPACE_02");
        spaceLink1.setTargetSpace(targetSpace1);
        spaceLinkDisplay1.setLink(spaceLink1);

        SpaceLinkDisplay spaceLinkDisplay2 = new SpaceLinkDisplay();
        SpaceLink spaceLink2 = new SpaceLink();
        spaceLink2.setSourceSpace(space);
        Space targetSpace2 = new Space();
        targetSpace2.setId("SPACE_03");
        spaceLink2.setTargetSpace(targetSpace2);
        spaceLinkDisplay2.setLink(spaceLink2);

        spaceToSpaceLinksList = new ArrayList<>();
        spaceToSpaceLinksList.add(spaceLinkDisplay1);
        spaceToSpaceLinksList.add(spaceLinkDisplay2);

        allSpacesList = new ArrayList<>();
        allSpacesList.add(space);
    }

    @Test
    public void test_getSpaceToModuleLinks_success() {

        when(moduleLinkDisplayRepository.findAll()).thenReturn(spaceToModuleLinksList);
        Map<String, Set<ModuleLinkDisplay>> spaceToModuleLinksMap = managerToTest.getSpaceToModuleLinks();
        assertEquals(spaceToModuleLinksMap.size(), 1);

        String key = null;
        Set<ModuleLinkDisplay> moduleLinkDisplaySet = null;
        for (Map.Entry<String, Set<ModuleLinkDisplay>> entry : spaceToModuleLinksMap.entrySet()) {
            key = entry.getKey();
            moduleLinkDisplaySet = entry.getValue();
        }
        assertEquals(key, "SPACE_01");
        assertEquals(moduleLinkDisplaySet.size(), 2);

        // As the insertion order is not preserved in moduleLinkDisplaySet so in order
        // to keep MODULE_01 first then MODULE_02 the below logic is done

        List<ModuleLinkDisplay> moduleLinkDisplayList = new ArrayList<>(moduleLinkDisplaySet);

        List<String> linkedModuleIdList = new ArrayList<>();
        linkedModuleIdList.add(moduleLinkDisplayList.get(0).getLink().getModule().getId());
        linkedModuleIdList.add(moduleLinkDisplayList.get(1).getLink().getModule().getId());

        String moduleIdArr[] = new String[2];
        if (linkedModuleIdList.contains("MODULE_01")) {
            moduleIdArr[0] = "MODULE_01";
            linkedModuleIdList.remove("MODULE_01");
        }
        if (linkedModuleIdList.contains("MODULE_02")) {
            moduleIdArr[1] = "MODULE_02";
            linkedModuleIdList.remove("MODULE_02");
        }

        assertArrayEquals(new String[] { "MODULE_01", "MODULE_02" }, moduleIdArr);
        assertEquals(linkedModuleIdList.size(), 0);
    }

    @Test
    public void test_getSpaceToSpaceLinks_success() {

        when(spaceLinkDisplayRepository.findAll()).thenReturn(spaceToSpaceLinksList);
        Map<String, Set<SpaceLinkDisplay>> spaceToSpaceLinksMap = managerToTest.getSpaceToSpaceLinks();
        assertEquals(spaceToSpaceLinksMap.size(), 1);

        String key = null;
        Set<SpaceLinkDisplay> spaceLinkDisplaySet = null;
        for (Map.Entry<String, Set<SpaceLinkDisplay>> entry : spaceToSpaceLinksMap.entrySet()) {
            key = entry.getKey();
            spaceLinkDisplaySet = entry.getValue();
        }
        assertEquals(key, "SPACE_01");
        assertEquals(spaceLinkDisplaySet.size(), 2);

        // As the insertion order is not preserved in spaceLinkDisplaySet so in order
        // to keep SPACE_02 first then SPACE_03 the below logic is done

        List<SpaceLinkDisplay> spaceLinkDisplayList = new ArrayList<>(spaceLinkDisplaySet);

        List<String> targetSpaceIdList = new ArrayList<>();
        targetSpaceIdList.add(spaceLinkDisplayList.get(0).getLink().getTargetSpace().getId());
        targetSpaceIdList.add(spaceLinkDisplayList.get(1).getLink().getTargetSpace().getId());

        String spaceIdArr[] = new String[2];
        if (targetSpaceIdList.contains("SPACE_02")) {
            spaceIdArr[0] = "SPACE_02";
            targetSpaceIdList.remove("SPACE_02");
        }
        if (targetSpaceIdList.contains("SPACE_03")) {
            spaceIdArr[1] = "SPACE_03";
            targetSpaceIdList.remove("SPACE_03");
        }

        assertArrayEquals(new String[] { "SPACE_02", "SPACE_03" }, spaceIdArr);
        assertEquals(targetSpaceIdList.size(), 0);
    }

    @Test
    public void test_getSpacesToSpacesAndModulesMap_success() {

        when(moduleLinkDisplayRepository.findAll()).thenReturn(spaceToModuleLinksList);
        Map<String, Set<ModuleLinkDisplay>> spaceToModuleLinksMap = managerToTest.getSpaceToModuleLinks();

        when(spaceLinkDisplayRepository.findAll()).thenReturn(spaceToSpaceLinksList);
        Map<String, Set<SpaceLinkDisplay>> spaceToSpaceLinksMap = managerToTest.getSpaceToSpaceLinks();

        when(spaceRepo.findAll()).thenReturn(allSpacesList);

        Map<String, List<String>> spaceLinkMap = managerToTest.getSpacesToSpacesAndModulesMap(spaceToModuleLinksMap,
                spaceToSpaceLinksMap);

        String key = null;
        List<String> spaceLinks = null;
        for (Map.Entry<String, List<String>> entry : spaceLinkMap.entrySet()) {
            key = entry.getKey();
            spaceLinks = entry.getValue();
        }
        assertEquals(key, "SPACE_01");
        assertEquals(spaceLinks.size(), 4);

        // As in spaceLinks there is no guarantee that the links ID will be in
        // a particular order so below logic is done to keep the order as MODULE_01,
        // MODULE_02, SPACE_02, SPACE_03

        String spaceLinksIdArr[] = new String[4];
        if (spaceLinks.contains("MODULE_01")) {
            spaceLinksIdArr[0] = "MODULE_01";
            spaceLinks.remove("MODULE_01");
        }
        if (spaceLinks.contains("MODULE_02")) {
            spaceLinksIdArr[1] = "MODULE_02";
            spaceLinks.remove("MODULE_02");
        }
        if (spaceLinks.contains("SPACE_02")) {
            spaceLinksIdArr[2] = "SPACE_02";
            spaceLinks.remove("SPACE_02");
        }
        if (spaceLinks.contains("SPACE_03")) {
            spaceLinksIdArr[3] = "SPACE_03";
            spaceLinks.remove("SPACE_03");
        }

        assertArrayEquals(new String[] { "MODULE_01", "MODULE_02", "SPACE_02", "SPACE_03" }, spaceLinksIdArr);
        assertEquals(spaceLinks.size(), 0);
    }
}
