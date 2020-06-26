package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.SpaceDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.SpaceDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.Sequence;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public class SpaceManagerTest {

    @Mock
    private SpaceRepository spaceRepo;

    @Mock
    private SpaceDisplayRepository spaceDisplayRepo;

    @Mock
    private ImageRepository imageRepo;

    @Mock
    private SpaceLinkRepository spaceLinkRepo;

    @Mock
    private SpaceLinkDisplayRepository spaceLinkDisplayRepo;

    @Mock
    private IStorageEngine storage;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private IImageService imageService;

    @Mock
    private ISpaceLinkFactory spaceLinkFactory;

    @Mock
    private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @Mock
    private ISpaceDisplayFactory spaceDisplayFactory;

    @Mock
    private ExhibitionManager exhibitionManager;


    @InjectMocks
    private SpaceManager managerToTest;

    private final String IMG_FILENAME = "img";
    private final String IMG_CONTENT_TYPE = "content/type";
    private String spaceId = "spaceId";
    private String spaceId1, spaceId2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spaceId1 = "SPA000000001";
        spaceId2 = "SPA000000001";

    }

    @Test
    public void test_storeSpace_success() throws FileStorageException {
        IVSImage image = new VSImage();
        image.setFilename(IMG_FILENAME);
        image.setFileType(IMG_CONTENT_TYPE);
        Mockito.when(imageFactory.createImage(Mockito.anyString(), Mockito.anyString())).thenReturn(image);
        Mockito.when(imageRepo.save((VSImage) image)).thenReturn((VSImage) image);
        ISpaceDisplay spaceDisplay = new SpaceDisplay();
        Mockito.when(spaceDisplayFactory.createSpaceDisplay()).thenReturn(spaceDisplay);
        Mockito.when(imageService.getImageData(Mockito.isA(byte[].class))).thenReturn(new ImageData(500, 300));

        byte[] imageBytes = new byte[100];
        Arrays.fill(imageBytes, (byte) 1);
        String filename = "IMAGE_FILE";

        String dirName = "DIR";
        String storePath = "PATH";

        Mockito.when(storage.storeFile(imageBytes, filename, dirName)).thenReturn(storePath);

        Space space = new Space();
        Mockito.when(spaceRepo.save((Space) space)).thenReturn(space);

        CreationReturnValue returnVal = managerToTest.storeSpace((ISpace) space, imageBytes, filename);
        Assert.assertEquals(returnVal.getElement(), space);
        Assert.assertEquals(returnVal.getErrorMsgs(), new ArrayList<>());
        Mockito.verify(spaceRepo).save(space);
        Mockito.verify(spaceDisplayRepo).save((SpaceDisplay)spaceDisplay);
    }

    @Test
    public void test_deleteSpaceById_whenIdIsNull() throws SpaceDoesNotExistException{
        Mockito.when(managerToTest.getSpace(spaceId)).thenReturn(null).thenReturn(null);
        Assert.assertEquals(null, managerToTest.getSpace(null));
        managerToTest.deleteSpaceById(null);
        Assert.assertEquals(null, managerToTest.getSpace(null));
    }

    @Test
    public void test_deleteSpaceById_forNonExistentId() {  
        Mockito.when(managerToTest.getSpace(spaceId)).thenReturn(null).thenReturn(null);
        Assert.assertEquals(null, managerToTest.getSpace(spaceId));
        managerToTest.deleteSpaceById(spaceId);
        Assert.assertEquals(null, managerToTest.getSpace(spaceId));
    }

    @Test
    public void test_deleteSpaceById_forSuccess() {  
        Space space = new Space();
        Optional<Space> spaceOpt = Optional.of(space);
        Mockito.when(spaceRepo.findById(spaceId1)).thenReturn(spaceOpt).thenReturn(null);
        Assert.assertEquals(space, managerToTest.getSpace(spaceId1));
        managerToTest.deleteSpaceById(spaceId1);
        Assert.assertEquals(null, managerToTest.getSpace(spaceId1));
    }
    
    @Test
    public void test_getSpacesWithStatus_whenStatusIsNull() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> nullStatusSpaces= managerToTest.getSpacesWithStatus(null);
        String actualSpaceIdWithNull = nullStatusSpaces.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithNull);
    }

    @Test
    public void test_getSpacesWithStatus_whenStatusAsPublished() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        String actualSpaceIdWithPubStatus = spaceWithPublishedStatus.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithPubStatus);
    }

    @Test
    public void test_getSpacesWithStatus_whenStatusAsUnPublished() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.UNPUBLISHED);
        String actualSpaceIdWithUnPubStatus = spaceWithPublishedStatus.get(0).getId();
        Assert.assertEquals(spaceId1, actualSpaceIdWithUnPubStatus);
    }

    @Test
    public void test_getSpacesWithPublishedStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        Space space2=new Space();
        space2.setId(spaceId2);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> spaceWithPublishedStatus= managerToTest.getSpacesWithStatus(SpaceStatus.PUBLISHED);
        Assert.assertTrue(spaceWithPublishedStatus.isEmpty());
    }

    @Test
    public void test_getSpacesWithNullStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.UNPUBLISHED);
        Space space2=new Space();
        space.setId(spaceId2);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        spaceList.add(space2);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.UNPUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        List<ISpace> spaceWithNullStatus= managerToTest.getSpacesWithStatus(null);
        Assert.assertTrue(spaceWithNullStatus.isEmpty());
    }

    @Test
    public void test_getSpacesWithUnPublishedStatus_spaceDoesNotExist() throws SpaceDoesNotExistException{
        Space space=new Space();
        space.setId(spaceId1);
        space.setSpaceStatus(SpaceStatus.PUBLISHED);
        Space space2=new Space();
        space2.setId(spaceId2);
        List<Space> spaceList=new ArrayList<>();
        spaceList.add(space);
        spaceList.add(space2);
        Mockito.when(spaceRepo.findAllBySpaceStatus(SpaceStatus.PUBLISHED)).thenReturn(spaceList);
        Mockito.when(spaceRepo.findAllBySpaceStatus(null)).thenReturn(spaceList);
        List<ISpace> spaceWithNullStatus= managerToTest.getSpacesWithStatus(SpaceStatus.UNPUBLISHED);
        Assert.assertTrue(spaceWithNullStatus.isEmpty());
    }

}
