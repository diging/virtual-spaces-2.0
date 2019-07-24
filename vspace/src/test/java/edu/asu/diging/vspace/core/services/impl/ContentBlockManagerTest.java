package edu.asu.diging.vspace.core.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.ModuleRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;

public class ContentBlockManagerTest {
    @Mock
    private ITextBlockFactory textBlockFactory;

    @Mock
    private IImageBlockFactory imageBlockFactory;

    @Mock
    private SlideManager slideManager;

    @Mock
    private ModuleManager moduleManager;

    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;

    @Mock
    private IImageFactory imageFactory;

    @Mock
    private ImageRepository imageRepo;

    @Mock
    private SlideRepository slideRepo;

    @Mock
    private ModuleRepository moduleRepo;

    @Mock
    private IStorageEngine storage;

    @InjectMocks
    private ContentBlockManager managerToTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test_deleteTextBlockById_success() throws BlockDoesNotExistException {
        String textBlockId = "2";
        managerToTest.deleteTextBlockById(textBlockId);
        Mockito.verify(textBlockRepo).deleteById(textBlockId);
    }

    @Test
    public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
        String imageBlockId = "2";
        managerToTest.deleteImageBlockById(imageBlockId);
        Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String textBlockId = "notARealId";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).deleteById(textBlockId);
        managerToTest.deleteTextBlockById(textBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        Mockito.doThrow(IllegalArgumentException.class).when(textBlockRepo).deleteById(null);
        managerToTest.deleteTextBlockById(null);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImageBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String imageBlockId = "notARealId";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(imageBlockRepo).deleteById(imageBlockId);
        managerToTest.deleteImageBlockById(imageBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        Mockito.doThrow(IllegalArgumentException.class).when(imageBlockRepo).deleteById(null);
        managerToTest.deleteImageBlockById(null);
    }

}
