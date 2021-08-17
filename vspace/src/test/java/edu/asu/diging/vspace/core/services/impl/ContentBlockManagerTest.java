package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.BiblioBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ReferenceRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ReferenceListDeletionForBiblioException;
import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.impl.BiblioBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;

public class ContentBlockManagerTest {
    
    @Mock
    private ContentBlockRepository contentBlockRepository;
    
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

    private ContentBlock contentBlock;
    
    private List<ContentBlock> contentBlockList;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        contentBlock = new ContentBlock();
        contentBlock.setContentOrder(1);
        
        ContentBlock contentBlock1 = new ContentBlock();
        contentBlock1.setContentOrder(2);
        ContentBlock contentBlock2 = new ContentBlock();
        contentBlock2.setContentOrder(3);
        contentBlockList = new ArrayList<>();
        contentBlockList.add(contentBlock1);
        contentBlockList.add(contentBlock2);
    }

    @Test
    public void test_deleteTextBlockById_success() throws BlockDoesNotExistException {
        String textBlockId = "textBlockId_2";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("textBlockId_2")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1",Integer.valueOf(1))).thenReturn(contentBlockList);
        managerToTest.deleteTextBlockById(textBlockId,"slideId_1");
        Mockito.verify(textBlockRepo).deleteById(textBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder()).collect(Collectors.toList());
        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));
        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteTextBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String textBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).deleteById(textBlockId);
        managerToTest.deleteTextBlockById(textBlockId,"slideId_1");
    }

    @Test
    public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String textBlockId = null;
        managerToTest.deleteTextBlockById(null,"slideId_1");
        Mockito.verify(textBlockRepo, Mockito.never()).deleteById(textBlockId);
    }

    @Test
    public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
        String imageBlockId = "imageBlockId_2";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("imageBlockId_2")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1",Integer.valueOf(1))).thenReturn(contentBlockList);
        managerToTest.deleteImageBlockById(imageBlockId,"slideId_1");
        Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder()).collect(Collectors.toList());
        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));
        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }

    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteImageBlockById_forNonExistentId() throws BlockDoesNotExistException {
        String imageBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(imageBlockRepo).deleteById(imageBlockId);
        managerToTest.deleteImageBlockById(imageBlockId,"slideId_1");
    }

    @Test
    public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
        String imageBlockId = null;
        managerToTest.deleteImageBlockById(null,"slideId_1");
        Mockito.verify(imageBlockRepo, Mockito.never()).deleteById(imageBlockId);
    }
    
    @Test
    public void test_deleteBiblioBlockById_success() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "CON000000002";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("CON000000002")).thenReturn(contentBlockOptional);
        when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1",Integer.valueOf(1))).thenReturn(contentBlockList);
        managerToTest.deleteBiblioBlockById(biblioBlockId,"slideId_1");
        Mockito.verify(biblioBlockRepo).deleteById(biblioBlockId);
        List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder()).collect(Collectors.toList());
        assertEquals(Integer.valueOf(1), contentOrderList.get(0));
        assertEquals(Integer.valueOf(2), contentOrderList.get(1));
        Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
    }
    
    @Test(expected = BlockDoesNotExistException.class)
    public void test_deleteBiblioBlockById_forNonExistentId() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = "notARealId";
        Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
        when(contentBlockRepository.findById("notARealId")).thenReturn(contentBlockOptional);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(biblioBlockRepo).deleteById(biblioBlockId);
        managerToTest.deleteBiblioBlockById(biblioBlockId, "slideId_1");
    }

    @Test
    public void test_deleteBiblioBlockById_whenIdIsNull() throws BlockDoesNotExistException, ReferenceListDeletionForBiblioException {
        String biblioBlockId = null;
        managerToTest.deleteBiblioBlockById(null, "slideId_1");
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
        
        IBiblioBlock biblioBlockWithId = new BiblioBlock();
        biblioBlockWithId.setId("CON00000001");
        biblioBlockWithId.setBiblioTitle("TestTitle");
        biblioBlockWithId.setDescription("Test Description");
        
        biblioBlock.setContentOrder(contentOrder);
        biblioBlock.setSlide(slide);
        
        when(slideManager.getSlide(slide.getId())).thenReturn(slide);
        when(biblioBlockRepo.save((BiblioBlock)biblioBlock)).thenReturn((BiblioBlock) biblioBlockWithId);
        IBiblioBlock resBiblio = managerToTest.createBiblioBlock(slide.getId(), biblioBlock, contentOrder);
        assertEquals(resBiblio.getId(), biblioBlockWithId.getId());
        assertEquals(resBiblio.getBiblioTitle(), biblioBlockWithId.getBiblioTitle());
        assertEquals(resBiblio.getDescription(), biblioBlockWithId.getDescription());
    }

}
