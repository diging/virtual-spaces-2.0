package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepo;
    
    @Mock
    private IStorageEngine storage;
    
    @InjectMocks
    private ImageService serviceToTest;

    private ImageForm imageForm;
    private List<VSImage> images;
    private IVSImage image;
    private final String IMG_ID = "id";
    private final String IMG_FILENAME = "img";
    private final String IMG_CONTENT_TYPE = "content/type";
    private final String NEW_IMG_FILENAME = "newImg";
    private final String DESCRIPTION = "description";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        image = new VSImage();
        image.setId(IMG_ID);
        image.setFilename(IMG_FILENAME);
        image.setFileType(IMG_CONTENT_TYPE);
        images = new ArrayList<>();
        images.add((VSImage)image);
        imageForm = new ImageForm();
        imageForm.setFileName(NEW_IMG_FILENAME);
        imageForm.setDescription(DESCRIPTION);
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 10);
    }

    @Test
    public void test_getImageData_success() throws IOException {
        InputStream fis = getClass().getResourceAsStream("/files/testImage.png");
        byte[] image = IOUtils.toByteArray(fis);

        ImageData data = serviceToTest.getImageData(image);
        Assert.assertEquals(500, data.getWidth());
        Assert.assertEquals(300, data.getHeight());
    }

    @Test
    public void test_getImageData_error() throws IOException {
        ImageData data = serviceToTest.getImageData(new byte[0]);
        Assert.assertNull(data);
    }

    @Test
    public void test_getImageDimensions_fullWidth() {
        IVSImage image = new VSImage();
        image.setWidth(500);
        image.setHeight(300);

        ImageData data = serviceToTest.getImageDimensions(image, 400, 300);
        Assert.assertEquals(400, data.getWidth());
        Assert.assertEquals(240, data.getHeight());
    }

    @Test
    public void test_getImageDimensions_fullHeight() {
        IVSImage image = new VSImage();
        image.setWidth(500);
        image.setHeight(300);

        ImageData data = serviceToTest.getImageDimensions(image, 400, 200);
        Assert.assertEquals(333, data.getWidth());
        Assert.assertEquals(200, data.getHeight());
    }
    
    @Test
    public void test_getTotalPages_success() {
        when(imageRepo.count()).thenReturn(12L);
        assertEquals(2, serviceToTest.getTotalPages());
    }
    
    @Test
    public void test_getTotalPages_whenZeroImages() {
        when(imageRepo.count()).thenReturn(0L);
        assertEquals(0, serviceToTest.getTotalPages());
    }
   
    @Test
    public void test_getImages_success() { 
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.CREATION_DATE.getValue()));
        when(imageRepo.count()).thenReturn(1L);
        when(imageRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<VSImage>(images));
        List<VSImage> requestedImages = serviceToTest.getImages(1);
        assertEquals(1, requestedImages.size());
        assertEquals(IMG_ID, requestedImages.get(0).getId());
        verify(imageRepo).findAll(sortByRequestedField);
    }
  
    @Test
    public void test_getImages_negativePage() { 
        Pageable sortByRequestedField = PageRequest.of(0, 10, Sort.by(SortByField.CREATION_DATE.getValue()));
        when(imageRepo.count()).thenReturn(1L);
        when(imageRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<VSImage>(images));
        List<VSImage> requestedImages = serviceToTest.getImages(-2);
        assertEquals(IMG_ID, requestedImages.get(0).getId());
        verify(imageRepo).findAll(sortByRequestedField);
    }
    
    @Test
    public void test_getImages_pageGreaterThanTotalPages() { 
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        Pageable sortByRequestedField = PageRequest.of(4, 1, Sort.by(SortByField.CREATION_DATE.getValue()));
        when(imageRepo.count()).thenReturn(5L);
        when(imageRepo.findAll(sortByRequestedField)).thenReturn(new PageImpl<VSImage>(images));
        List<VSImage> requestedImages = serviceToTest.getImages(7);
        assertEquals(IMG_ID, requestedImages.get(0).getId());
        verify(imageRepo).findAll(sortByRequestedField);
    }
    
    @Test
    public void test_getTotalImageCount_success() {
        when(imageRepo.count()).thenReturn(5L);
        assertEquals(5L, serviceToTest.getTotalImageCount());
    }
    
    @Test
    public void test_getTotalImageCount_whenZeroImages() {
        when(imageRepo.count()).thenReturn(0L);
        assertEquals(0L, serviceToTest.getTotalImageCount());
    }
    
    @Test
    public void test_validatePageNumber_success() {
        when(imageRepo.count()).thenReturn(40L);
        assertEquals(2, serviceToTest.validatePageNumber(2));
    }
    
    @Test
    public void test_validatePageNumber_whenPageIsNegative() {
        when(imageRepo.count()).thenReturn(1L);
        assertEquals(1, serviceToTest.validatePageNumber(-1));
    }
    
    @Test
    public void test_validatePageNumber_pageGreaterThanTotalPages() {
        ReflectionTestUtils.setField(serviceToTest, "pageSize", 1);
        when(imageRepo.count()).thenReturn(5L);
        assertEquals(5, serviceToTest.validatePageNumber(20));
    }
    
    @Test
    public void test_editImage_success() throws ImageDoesNotExistException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.of(images.get(0)));
        serviceToTest.editImage(IMG_ID, imageForm);
        Assert.assertEquals(imageForm.getName(), image.getName());
        Assert.assertEquals(imageForm.getDescription(), image.getDescription());
    }
    
    @Test(expected = ImageDoesNotExistException.class)
    public void test_editImage_whenNoImageExist() throws ImageDoesNotExistException{
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.empty());
        serviceToTest.editImage(IMG_ID, imageForm);
    }
    
    @Test
    public void test_getImageById_success() throws ImageDoesNotExistException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.of(images.get(0)));
        assertEquals(images.get(0).getId(), serviceToTest.getImageById(IMG_ID).getId());
    }
    
    @Test(expected = ImageDoesNotExistException.class)
    public void test_getImageById_whenNoImageExist() throws ImageDoesNotExistException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.empty());
        serviceToTest.getImageById(IMG_ID);
    }

}
