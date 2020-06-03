package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;

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
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.model.impl.Space;
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


    @InjectMocks
    private SpaceManager managerToTest;

    private final String IMG_FILENAME = "img";
    private final String IMG_CONTENT_TYPE = "content/type";
    private String spaceId = "spaceId";
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
    

    
    @Test(expected = SpaceDoesNotExistException.class)
    public void test_deleteSpaceById_whenIdIsNull() throws SpaceDoesNotExistException{
       Mockito.doThrow(IllegalArgumentException.class)
        .when(spaceRepo).deleteById(null);
    }
    
    @Test(expected = SpaceDoesNotExistException.class)
    public void test_deleteSpaceById_forNonExistentId() throws SpaceDoesNotExistException {  
       Mockito.doThrow(EmptyResultDataAccessException.class)
        .when(spaceRepo).deleteById(spaceId);
    }    
}