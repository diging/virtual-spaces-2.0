package edu.asu.diging.vspace.core.services.impl;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.impl.ImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

public class ImageFactoryTest {

    private final String IMG_ID = "id";
    private final String IMG_FILENAME = "img";
    private final String NEW_IMG_FILENAME = "newImg";
    private final String IMG_CONTENT_TYPE = "content/type";
    private final String DESCRIPTION = "description";
    private VSImage image;
    private ImageForm imageForm;
    
    @Mock
    private ImageRepository imageRepo;
    
    @Mock
    private IStorageEngine storage;
    
    @InjectMocks
    private ImageFactory factoryToTest;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        image = new VSImage();
        image.setId(IMG_ID);
        image.setFilename(IMG_FILENAME);
        image.setFileType(IMG_CONTENT_TYPE);
        imageForm = new ImageForm();
        imageForm.setFileName(NEW_IMG_FILENAME);
        imageForm.setDescription(DESCRIPTION);
    }
    
    @Test(expected = Test.None.class)
    public void test_editImage_success() throws FileNotFoundException, FileStorageException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.of(image));
        Mockito.when(storage.renameImage(image, imageForm.getFileName())).thenReturn(true);
        factoryToTest.editImage(IMG_ID, imageForm);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void test_editImage_whenNoImageExist() throws FileNotFoundException, FileStorageException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.empty());
        factoryToTest.editImage(IMG_ID, imageForm);
    }
    
    @Test(expected = FileStorageException.class)
    public void test_editImage_whenRenameFails() throws FileNotFoundException, FileStorageException {
        Mockito.when(imageRepo.findById(IMG_ID)).thenReturn(Optional.of(image));
        Mockito.when(storage.renameImage(image, imageForm.getFileName())).thenReturn(false);
        factoryToTest.editImage(IMG_ID, imageForm);
    }
}
