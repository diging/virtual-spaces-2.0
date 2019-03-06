package edu.asu.diging.vspace.core.services.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public class ImageServiceTest {

    @InjectMocks
    private ImageService serviceToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
}
