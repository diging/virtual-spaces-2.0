package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;

public class ContentBlockManagerTest {
    
    @Mock
    private ContentBlockRepository contentBlockRepository;
    
    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;

    @InjectMocks
    private ContentBlockManager managerToTest;

    private ContentBlock contentBlock;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        contentBlock = new ContentBlock();
        contentBlock.setContentOrder(1);
    }

    @Test
    public void test_deleteTextBlockById_success() throws BlockDoesNotExistException {
        String textBlockId = "2";
        Optional<ContentBlock> ContentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("2")).thenReturn(ContentBlockOptional);
        managerToTest.deleteTextBlockById(textBlockId,null);
        Mockito.verify(textBlockRepo).deleteById(textBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String textBlockId = "notARealId";
        Optional<ContentBlock> ContentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(ContentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).deleteById(textBlockId);
        managerToTest.deleteTextBlockById(textBlockId,null);
    }

    @Test
    public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String textBlockId = null;
        managerToTest.deleteTextBlockById(null,null);
        Mockito.verify(textBlockRepo, Mockito.never()).deleteById(textBlockId);
    }

    @Test
    public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
        String imageBlockId = "2";
        Optional<ContentBlock> ContentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("2")).thenReturn(ContentBlockOptional);
        managerToTest.deleteImageBlockById(imageBlockId,null);
        Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImageBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String imageBlockId = "notARealId";
        Optional<ContentBlock> ContentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(ContentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(imageBlockRepo).deleteById(imageBlockId);
        managerToTest.deleteImageBlockById(imageBlockId,null);
    }

    @Test
    public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String imageBlockId = null;
        managerToTest.deleteImageBlockById(null,null);
        Mockito.verify(imageBlockRepo, Mockito.never()).deleteById(imageBlockId);

    }

}
