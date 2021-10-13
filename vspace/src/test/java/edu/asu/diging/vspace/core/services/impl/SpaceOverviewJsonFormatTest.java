package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.model.impl.Module;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class SpaceOverviewJsonFormatTest {

    @Mock
    private SpaceRepository spaceRepo;

    @Mock
    private ModuleRepository moduleRepo;

    @InjectMocks
    private SpaceOverviewJsonFormat serviceToTest;

    private List<Module> allModulesList;

    private List<Space> allSpacesList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Module module = new Module();
        module.setId("MODULE_ID1");
        module.setName("MODULE_1");
        allModulesList = new ArrayList<>();
        allModulesList.add(module);

        Space space = new Space();
        space.setId("SPACE_ID1");
        space.setName("SPACE_1");
        VSImage image = new VSImage();
        image.setId("IMG_ID1");
        space.setImage(image);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        space.setHideIncomingLinks(false);
        allSpacesList = new ArrayList<>();
        allSpacesList.add(space);

    }

    @Test
    public void test_createNodes_success() {

        when(moduleRepo.findAll()).thenReturn(allModulesList);
        when(spaceRepo.findAll()).thenReturn(allSpacesList);

        Map<String, List<String>> spacesToSpacesAndModulesMap = new HashMap<>();
        spacesToSpacesAndModulesMap.put("SPACE_ID1",
                new ArrayList<>((List.of("MODULE_ID1", "MODULE_ID5", "MODULE_ID4", "SPACE_ID2"))));
        spacesToSpacesAndModulesMap.put("MODULE_ID1", null);
        try {
            String json = serviceToTest.createNodes("contextPath", spacesToSpacesAndModulesMap);
            JsonNode tree1 = new ObjectMapper().readTree(json);
            String expectedJson = "[{\"name\":\"SPACE_1\",\"id\":\"SPACE_ID1\",\"link\":\"contextPath/staff/space/SPACE_ID1\",\"img\":\"contextPath/api/image/IMG_ID1\",\"module\":false,\"unpublished\":false,\"hideIncomingLinks\":false,\"edges\":[\"MODULE_ID1\",\"MODULE_ID5\",\"MODULE_ID4\",\"SPACE_ID2\"]},"
                    + "{\"name\":\"MODULE_1\",\"id\":\"MODULE_ID1\",\"link\":\"contextPath/staff/module/MODULE_ID1\",\"img\":\"\",\"module\":true,\"unpublished\":false,\"hideIncomingLinks\":false,\"edges\":null}]";
            JsonNode tree2 = new ObjectMapper().readTree(expectedJson);
            assertEquals(tree1, tree2);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
