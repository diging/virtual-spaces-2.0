package edu.asu.diging.vspace.core.services.impl;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ReferenceListDeletionForBiblioException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;

public class ContentBlockManagerTest {
    @Mock
    private TextContentBlockRepository textBlockRepo;

    @Mock
    private ImageContentBlockRepository imageBlockRepo;
    
    @Mock
    private BiblioBlockRepository biblioBlockRepo;
    
    @Mock
    private ReferenceRepository refRepo;
    
    @Mock
    private SlideManager slideManager;
    
    @Mock
    private ReferenceManager refManager;
    
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

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String textBlockId = "notARealId";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).deleteById(textBlockId);
        managerToTest.deleteTextBlockById(textBlockId);
    }

    @Test
    public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String textBlockId = null;
        managerToTest.deleteTextBlockById(null);
        Mockito.verify(textBlockRepo, Mockito.never()).deleteById(textBlockId);
    }

    @Test
    public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
        String imageBlockId = "2";
        managerToTest.deleteImageBlockById(imageBlockId);
        Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImageBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String imageBlockId = "notARealId";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(imageBlockRepo).deleteById(imageBlockId);
        managerToTest.deleteImageBlockById(imageBlockId);
    }

    @Test
    public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String imageBlockId = null;
        managerToTest.deleteImageBlockById(null);
        Mockito.verify(imageBlockRepo, Mockito.never()).deleteById(imageBlockId);
    }
    
    @Test
    public void test_deleteBiblioBlockById_success() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "CON000000002";
        managerToTest.deleteBiblioBlockById(biblioBlockId);
        Mockito.verify(biblioBlockRepo).deleteById(biblioBlockId);
    }
    
    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteBiblioBlockById_forNonExistentId() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "notARealId";
        Mockito.doThrow(EmptyResultDataAccessException.class).when(biblioBlockRepo).deleteById(biblioBlockId);
        managerToTest.deleteBiblioBlockById(biblioBlockId);
    }

    @Test
    public void test_deleteBiblioBlockById_whenIdIsNull() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = null;
        managerToTest.deleteBiblioBlockById(null);
        Mockito.verify(biblioBlockRepo, Mockito.never()).deleteById(biblioBlockId);
    }
    
    @Test
    public void test_createBiblioBlock_success() {

        Integer contentOrder = 1;

        Slide slide = new Slide();
        slide.setId("slide1");
        
        IBiblioBlock biblioBlock = new BiblioBlock();
        biblioBlock.setBiblioTitle("TestTitle");
        biblioBlock.setDescription("Test Description");
        
        biblioBlock.setContentOrder(contentOrder);
        biblioBlock.setSlide(slide);
        
        when(slideManager.getSlide(slide.getId())).thenReturn(slide);
        IBiblioBlock resBiblio = managerToTest.createBiblioBlock(slide.getId(), biblioBlock);
        Mockito.verify(biblioBlockRepo).save((BiblioBlock)biblioBlock);
        
    }

}
