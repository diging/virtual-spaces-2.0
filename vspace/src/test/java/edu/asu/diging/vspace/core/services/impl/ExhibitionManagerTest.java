package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ExhibitionRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IExhibition;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.Exhibition;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public class ExhibitionManagerTest {

    @Mock
    private ExhibitionRepository exhibitRepo;

    @Mock
    private IStorageEngine storage;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private IImageService imageService;

    @Mock
    private ImageRepository imageRepo;

    @InjectMocks
    private ExhibitionManager serviceToTest;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

    }

    private final String IMG_CONTENT_TYPE = "image/png";
    private final String FILENAME = "IMAGE_FILE";
    private final String ID = "IMAGE_ID";
    private final String STORE_PATH = "PATH";

    @Test
    public void test_storeExhibition_success() {
        Exhibition exhibition = new Exhibition();
        when(exhibitRepo.save(exhibition)).thenReturn(exhibition);
        IExhibition exhibitionTest = serviceToTest.storeExhibition(exhibition);
        assertNotNull(exhibitionTest);
        verify(exhibitRepo).save(exhibition);
    }

    @Test
    public void test_getExhibitionById_success() {
        String id = "ID";
        Exhibition exhibition = new Exhibition();
        exhibition.setId(id);
        Optional<Exhibition> exhibitionOptional = Optional.of(exhibition);
        ;
        when(exhibitRepo.findById(id)).thenReturn(exhibitionOptional);

        IExhibition exhibitionTest = serviceToTest.getExhibitionById(id);
        assertEquals(exhibitionTest, exhibition);
        verify(exhibitRepo).findById(id);
    }

    @Test
    public void test_storeDefaultImage_success() throws FileStorageException, IOException {
        InputStream fis = getClass().getResourceAsStream("/files/testImage.png");
        byte[] imageBytes = IOUtils.toByteArray(fis);
        
        ImageData data = new ImageData();
        data.setHeight(200);
        data.setWidth(400);

        IVSImage defaultImage = new VSImage();
        defaultImage.setId(ID);
        defaultImage.setFilename(FILENAME);
        defaultImage.setHeight(200);
        defaultImage.setWidth(400);
        defaultImage.setFileType(IMG_CONTENT_TYPE);

        Mockito.when(imageFactory.createDefaultImage(FILENAME, IMG_CONTENT_TYPE, ID)).thenReturn(defaultImage);
        Mockito.when(imageRepo.save((VSImage) defaultImage)).thenReturn((VSImage) defaultImage);
        Mockito.when(storage.storeFile(imageBytes, FILENAME, ID)).thenReturn(STORE_PATH);
        Mockito.when(imageService.getImageData(imageBytes)).thenReturn(data);
        IVSImage returnVal = serviceToTest.storeDefaultImage(imageBytes, FILENAME, ID);

        Assert.assertNotNull(returnVal);

    }
    
    @Test(expected = FileStorageException.class)
    public void test_storeDefaultImage_Error() throws FileStorageException, IOException {
        InputStream fis = getClass().getResourceAsStream("/files/testImage.png");
        byte[] imageBytes = IOUtils.toByteArray(fis);
        
        ImageData data = new ImageData();
        data.setHeight(200);
        data.setWidth(400);

        IVSImage defaultImage = new VSImage();
        defaultImage.setId(ID);
        defaultImage.setFilename(FILENAME);
        defaultImage.setHeight(200);
        defaultImage.setWidth(400);
        defaultImage.setFileType(IMG_CONTENT_TYPE);

        Mockito.when(imageFactory.createDefaultImage(FILENAME, IMG_CONTENT_TYPE, ID)).thenReturn(defaultImage);
        Mockito.when(imageRepo.save((VSImage) defaultImage)).thenReturn((VSImage) defaultImage);
        Mockito.when(storage.storeFile(imageBytes, FILENAME, ID)).thenThrow(FileStorageException.class);
        Mockito.when(imageService.getImageData(imageBytes)).thenReturn(data);
        IVSImage returnVal = serviceToTest.storeDefaultImage(imageBytes, FILENAME, ID);

        Assert.assertNotNull(returnVal);

    }
    

}
