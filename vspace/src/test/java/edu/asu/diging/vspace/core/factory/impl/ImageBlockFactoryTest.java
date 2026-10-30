package edu.asu.diging.vspace.core.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

@RunWith(MockitoJUnitRunner.class)
public class ImageBlockFactoryTest {

    @Mock
    private ISlide mockSlide;

    @Mock
    private IVSImage mockImage;

    @InjectMocks
    private ImageBlockFactory imageBlockFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createImageBlock_success() {
        IImageBlock imageBlock = imageBlockFactory.createImageBlock(mockSlide, mockImage);
        assertNotNull(imageBlock);
        assertEquals(mockSlide, imageBlock.getSlide());
        assertEquals(mockImage, imageBlock.getImage());
    }

}
