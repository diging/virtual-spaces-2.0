package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IVSImage;

@RunWith(MockitoJUnitRunner.class)
public class ImageFactoryTest {

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
        // Call the method being tested
        IVSImage image = imageFactory.createImage(IMAGENAME, IMAGETYPE);

        // Verify the results
        assertEquals(IMAGENAME, image.getFilename());
        assertEquals(IMAGETYPE, image.getFileType());
    }

    @Test
    public void test_createImage_nullFilename() {
        // Call the method being tested
        IVSImage image = imageFactory.createImage(null, IMAGETYPE);

        // Verify the results
        assertNull(image.getFilename());
        assertEquals(IMAGETYPE, image.getFileType());
    }

    @Test
    public void test_createImage_nullFileType() {
        // Call the method being tested
        IVSImage image = imageFactory.createImage(IMAGENAME, null);

        // Verify the results
        assertEquals(IMAGENAME, image.getFilename());
        assertNull(image.getFileType());
    }
}
