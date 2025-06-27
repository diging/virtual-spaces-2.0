package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.model.IVSImage;

@RunWith(MockitoJUnitRunner.class)
public class ImageFactoryTest {

    @Mock
    IVSImage imageMock;

    @Mock
    IImageFactory imageFactoryMock;

    @InjectMocks
    ImageFactory imageFactory;

    private final String IMAGENAME = "test.jpg";
    private final String IMAGETYPE = "jpg";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createImage_success() {
        // Configure the mock objects
        when(imageMock.getFilename()).thenReturn(IMAGENAME);
        when(imageMock.getFileType()).thenReturn(IMAGETYPE);
        when(imageFactoryMock.createImage(IMAGENAME, IMAGETYPE)).thenReturn(imageMock);

        // Call the method being tested
        IVSImage image = imageFactory.createImage(IMAGENAME, IMAGETYPE);

        // Verify the results
        assertEquals(IMAGENAME, image.getFilename());
        assertEquals(IMAGETYPE, image.getFileType());
    }

    @Test
    public void test_createImage_nullFilename() {
        // Configure the mock objects
        when(imageMock.getFilename()).thenReturn(null);
        when(imageMock.getFileType()).thenReturn(IMAGETYPE);
        when(imageFactoryMock.createImage(null, IMAGETYPE)).thenReturn(imageMock);

        // Call the method being tested
        IVSImage image = imageFactory.createImage(null, IMAGETYPE);

        // Verify the results
        assertNull(image.getFilename());
        assertEquals(IMAGETYPE, image.getFileType());
    }

    @Test
    public void test_createImage_nullFileType() {
        // Configure the mock objects
        when(imageMock.getFilename()).thenReturn(IMAGENAME);
        when(imageMock.getFileType()).thenReturn(null);
        when(imageFactoryMock.createImage(IMAGENAME, null)).thenReturn(imageMock);

        // Call the method being tested
        IVSImage image = imageFactory.createImage(IMAGENAME, null);

        // Verify the results
        assertEquals(IMAGENAME, image.getFilename());
        assertNull(image.getFileType());
    }
}
