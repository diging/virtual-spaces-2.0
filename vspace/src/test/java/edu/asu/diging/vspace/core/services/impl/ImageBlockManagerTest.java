package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class ImageBlockManagerTest {

    private static final String SLIDE_ID = "SLI000000001";
    private static final String IMAGE_ID = "IMG000000001";
    private static final byte[] PNG_BYTES = new byte[] { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1a, '\n', 0, 0, 0, 0 };

    @Mock
    private ISlideManager slideManager;

    @Mock
    private ContentBlockRepository contentBlockRepository;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;

    @Mock
    private ImageRepository imageRepo;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private IImageBlockFactory imageBlockFactory;

    @Mock
    private IStorageEngine storage;

    @InjectMocks
    private ImageBlockManager managerToTest;

    private Slide slide;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        slide = new Slide();
        when(slideManager.getSlide(SLIDE_ID)).thenReturn(slide);
        // Mockito returns 0 for unstubbed Integer methods; null means the slide has no blocks yet
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(null);
        when(imageBlockRepo.save(any(ImageBlock.class))).thenAnswer(i -> i.getArgument(0));
        when(imageRepo.save(any(VSImage.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    public void test_createImageBlock_withUploadedFile() throws Exception {
        VSImage image = new VSImage();
        image.setId(IMAGE_ID);
        when(imageFactory.createImage("photo.png", "image/png")).thenReturn(image);
        when(storage.storeFile(PNG_BYTES, "photo.png", IMAGE_ID)).thenReturn("stored/path");
        ImageBlock created = new ImageBlock();
        when(imageBlockFactory.createImageBlock(slide, image)).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(1);

        CreationReturnValue result = managerToTest.createImageBlock(SLIDE_ID, PNG_BYTES, "photo.png");

        assertSame(created, result.getElement());
        assertTrue(result.getErrorMsgs().isEmpty());
        assertEquals(Integer.valueOf(2), created.getContentOrder());
        assertEquals("stored/path", image.getParentPath());
        verify(imageBlockRepo).save(created);
    }

    @Test
    public void test_createImageBlock_withoutFileSkipsStorage() throws Exception {
        ImageBlock created = new ImageBlock();
        when(imageBlockFactory.createImageBlock(eq(slide), isNull())).thenReturn(created);

        CreationReturnValue result = managerToTest.createImageBlock(SLIDE_ID, null, "default.jpg");

        assertSame(created, result.getElement());
        assertEquals(Integer.valueOf(0), created.getContentOrder());
        verify(storage, never()).storeFile(any(), anyString(), anyString());
        verify(imageRepo, never()).save(any());
    }

    @Test(expected = ImageCouldNotBeStoredException.class)
    public void test_createImageBlock_storageFailure() throws Exception {
        VSImage image = new VSImage();
        image.setId(IMAGE_ID);
        when(imageFactory.createImage("photo.png", "image/png")).thenReturn(image);
        when(storage.storeFile(PNG_BYTES, "photo.png", IMAGE_ID)).thenThrow(new FileStorageException("disk full"));

        managerToTest.createImageBlock(SLIDE_ID, PNG_BYTES, "photo.png");
    }

    @Test
    public void test_createImageBlock_withExistingImage() {
        VSImage image = new VSImage();
        ImageBlock created = new ImageBlock();
        when(imageBlockFactory.createImageBlock(slide, image)).thenReturn(created);
        when(contentBlockRepository.findMaxContentOrder(SLIDE_ID)).thenReturn(6);

        CreationReturnValue result = managerToTest.createImageBlock(SLIDE_ID, image);

        assertSame(created, result.getElement());
        assertEquals(Integer.valueOf(7), created.getContentOrder());
        verify(imageRepo, never()).save(any());
    }

    @Test
    public void test_createContentBlock_createsBlockWithoutImage() {
        ImageBlock created = new ImageBlock();
        when(imageBlockFactory.createImageBlock(eq(slide), isNull())).thenReturn(created);

        IImageBlock result = managerToTest.createContentBlock(SLIDE_ID);

        assertSame(created, result);
    }

    @Test
    public void test_updateImageBlock_withUploadedFile() throws Exception {
        VSImage image = new VSImage();
        image.setId(IMAGE_ID);
        when(imageFactory.createImage("new.png", "image/png")).thenReturn(image);
        when(storage.storeFile(PNG_BYTES, "new.png", IMAGE_ID)).thenReturn("new/path");
        ImageBlock block = new ImageBlock();

        managerToTest.updateImageBlock(block, PNG_BYTES, "new.png");

        assertSame(image, block.getImage());
        assertEquals("new/path", image.getParentPath());
        verify(imageBlockRepo).save(block);
    }

    @Test
    public void test_updateImageBlock_withExistingImage() {
        IVSImage image = new VSImage();
        ImageBlock block = new ImageBlock();

        managerToTest.updateImageBlock(block, image);

        assertSame(image, block.getImage());
        verify(imageBlockRepo).save(block);
    }
}
