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

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SpaceLinkRepository;
import edu.asu.diging.vspace.core.data.SpaceRepository;
import edu.asu.diging.vspace.core.data.display.SpaceLinkDisplayRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.VSImage;

public class SpaceManagerTest {

    @Mock
    private SpaceRepository spaceRepo;

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
    private ISpaceLinkFactory spaceLinkFactory;

    @Mock
    private ISpaceLinkDisplayFactory spaceLinkDisplayFactory;

    @InjectMocks
    private SpaceManager managerToTest;

    private final String IMG_FILENAME = "img";
    private final String IMG_CONTENT_TYPE = "content/type";

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
        ;
    }
}
