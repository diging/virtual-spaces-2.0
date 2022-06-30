package edu.asu.diging.vspace.core.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import edu.asu.diging.vspace.core.data.ChoiceContentBlockRepository;
import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.SlideRepository;
import edu.asu.diging.vspace.core.data.SpaceContentBlockRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.BlockDoesNotExistException;
import edu.asu.diging.vspace.core.exception.ImageCouldNotBeStoredException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.impl.SpaceBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.core.model.impl.SpaceBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.ISlideManager;

public class ContentBlockManagerTest {

	@Mock
	private ContentBlockRepository contentBlockRepository;

	@Mock
	private TextContentBlockRepository textBlockRepo;

	@Mock
	private SpaceContentBlockRepository spaceBlockRepo;

	@Mock
	private ImageContentBlockRepository imageBlockRepo;

	@Mock
	private SlideRepository slideRepo;

	@Mock
	private ISlideManager slideManager;

	@Mock
	private IImageBlockFactory imageBlockFactory;

	@Mock
	private SpaceBlockFactory spaceBlockFactory;

	@Mock
	private ChoiceContentBlockRepository choiceBlockRepo;

	@Mock
	private IImageFactory imageFactory;

	@Mock
	private ImageRepository imageRepo;

	@InjectMocks
	private ContentBlockManager managerToTest;

	private ContentBlock contentBlock;

	private ContentBlock contentBlock1;

	private List<ContentBlock> contentBlockList;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		contentBlock = new ContentBlock();
		contentBlock.setContentOrder(1);

		contentBlock1 = new ContentBlock();
		contentBlock1.setContentOrder(2);

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
		when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1", Integer.valueOf(1)))
				.thenReturn(contentBlockList);
		managerToTest.deleteTextBlockById(textBlockId, "slideId_1");
		Mockito.verify(textBlockRepo).deleteById(textBlockId);
		List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder())
				.collect(Collectors.toList());
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
		Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).existsById(textBlockId);
		managerToTest.deleteTextBlockById(textBlockId, "slideId_1");
	}

	@Test(expected = BlockDoesNotExistException.class)
	public void test_deleteSpaceBlockById_forNonExistentId() throws BlockDoesNotExistException {
		String spaceBlockId = "notARealId";
		when(contentBlockRepository.findById("notARealId")).thenReturn(Optional.empty());
		Mockito.doThrow(EmptyResultDataAccessException.class).when(spaceBlockRepo).deleteById(spaceBlockId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(textBlockRepo).existsById(spaceBlockId);
		managerToTest.deleteSpaceBlockById(spaceBlockId, "slideId_1");
	}

	@Test
	public void test_deleteSpaceBlock_success() throws BlockDoesNotExistException {
		String spaceBlockId = "realId";
		when(contentBlockRepository.findById(spaceBlockId)).thenReturn(Optional.of(contentBlock));
		managerToTest.deleteSpaceBlockById(spaceBlockId, "slideId_1");
		Mockito.verify(spaceBlockRepo).deleteById(spaceBlockId);

	}

	@Test
	public void test_createSpaceBlock_success() {
		String slideId = "slideId";
		ISlide slide = null;
		String title = "this is a space block";
		Integer contentOrder = 2;
		Space space = new Space();
		ISpaceBlock spaceBlock = new SpaceBlock();
		Mockito.when(slideManager.getSlide(slideId)).thenReturn(slide);
		Mockito.when(spaceBlockRepo.save((SpaceBlock) spaceBlock)).thenReturn((SpaceBlock) spaceBlock);
		Mockito.when(spaceBlockFactory.createSpaceBlock(slide, title, (ISpace) space)).thenReturn(spaceBlock);
		ISpaceBlock createdBlock = managerToTest.createSpaceBlock(slideId, title, contentOrder, space);
		Assert.assertEquals(createdBlock.getContentOrder(), contentOrder);
	}

	@Test
	public void test_getSpaceBlock_success() {
		String spaceId = "spaceId";
		SpaceBlock spaceBlock = new SpaceBlock();
		spaceBlock.setId(spaceId);
		when(spaceBlockRepo.findById(spaceId)).thenReturn(Optional.of(spaceBlock));
		ISpaceBlock retrievedSpaceBlock = managerToTest.getSpaceBlock(spaceId);
		assertEquals(spaceId, retrievedSpaceBlock.getId());
	}

	@Test
	public void test_getSpaceBlock_NonExistentId() throws BlockDoesNotExistException {
		String spaceId = "notRealId";
		when(spaceBlockRepo.findById(spaceId)).thenReturn(Optional.empty());
		ISpaceBlock retrievedSpaceBlock = managerToTest.getSpaceBlock(spaceId);
		assertNull(retrievedSpaceBlock);

	}

	@Test
	public void test_updateSpaceBlock_success() {
		String upadtedSpaceId = "spaceId1";
		SpaceBlock spaceBlock = new SpaceBlock();
		spaceBlock.setId(upadtedSpaceId);
		Mockito.when(spaceBlockRepo.save(spaceBlock)).thenReturn(spaceBlock);
		managerToTest.updateSpaceBlock(spaceBlock);
		Mockito.verify(spaceBlockRepo).save(spaceBlock);

	}

	@Test
	public void test_deleteTextBlockById_whenIdIsNull() throws BlockDoesNotExistException {
		String textBlockId = null;
		managerToTest.deleteTextBlockById(null, "slideId_1");
		Mockito.verify(textBlockRepo, Mockito.never()).deleteById(textBlockId);
	}

	@Test
	public void test_deleteImageBlockById_success() throws BlockDoesNotExistException {
		String imageBlockId = "imageBlockId_2";
		Optional<ContentBlock> contentBlockOptional = Optional.of(contentBlock);
		when(contentBlockRepository.findById("imageBlockId_2")).thenReturn(contentBlockOptional);
		when(contentBlockRepository.findBySlide_IdAndContentOrderGreaterThan("slideId_1", Integer.valueOf(1)))
				.thenReturn(contentBlockList);
		managerToTest.deleteImageBlockById(imageBlockId, "slideId_1");
		Mockito.verify(imageBlockRepo).deleteById(imageBlockId);
		List<Integer> contentOrderList = contentBlockList.stream().map(contentBlock -> contentBlock.getContentOrder())
				.collect(Collectors.toList());
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
		managerToTest.deleteImageBlockById(imageBlockId, "slideId_1");
	}

	@Test
	public void test_deleteImagetBlockById_whenIdIsNull() throws BlockDoesNotExistException {
		String imageBlockId = null;
		managerToTest.deleteImageBlockById(null, "slideId_1");
		Mockito.verify(imageBlockRepo, Mockito.never()).deleteById(imageBlockId);

	}

	@Test
	public void test_updateContentOrder_success() throws BlockDoesNotExistException {
		ContentBlock firstContentBlock = new ContentBlock();
		firstContentBlock.setId("contentBlockId1");
		firstContentBlock.setContentOrder(Integer.valueOf(3));

		ContentBlock secondContentBlock = new ContentBlock();
		secondContentBlock.setId("contentBlockId2");
		secondContentBlock.setContentOrder(Integer.valueOf(4));

		List<ContentBlock> contentBlocks = new ArrayList<>();
		contentBlocks.add(firstContentBlock);
		contentBlocks.add(secondContentBlock);

		when(contentBlockRepository.findById("contentBlockId1")).thenReturn(Optional.of(contentBlock));
		when(contentBlockRepository.findById("contentBlockId2")).thenReturn(Optional.of(contentBlock1));

		managerToTest.updateContentOrder(contentBlocks);
		assertEquals(Integer.valueOf(3), contentBlock.getContentOrder());
		assertEquals(Integer.valueOf(4), contentBlock1.getContentOrder());

		List<ContentBlock> contentBlockList = new ArrayList<>();
		contentBlockList.add(contentBlock);
		contentBlockList.add(contentBlock1);

		Mockito.verify(contentBlockRepository).saveAll(contentBlockList);
	}

	@Test(expected = BlockDoesNotExistException.class)
	public void test_updateContentOrder_forNonExistentId() throws BlockDoesNotExistException {
		ContentBlock contentBlock1 = new ContentBlock();
		contentBlock1.setId("notARealId");
		contentBlock1.setContentOrder(Integer.valueOf(1));

		List<ContentBlock> contentBlocks = new ArrayList<>();
		contentBlocks.add(contentBlock1);

		when(contentBlockRepository.findById("notARealId")).thenReturn(Optional.empty());
		managerToTest.updateContentOrder(contentBlocks);
	}

	@Test
	public void test_getAllContentBlocks_success() throws BlockDoesNotExistException {

		String slideId = "slide1";
		ISlide slide = new Slide();
		slide.setId(slideId);
		ContentBlock firstContentBlock = new ContentBlock();
		firstContentBlock.setId("contentBlockId1");
		firstContentBlock.setContentOrder(Integer.valueOf(3));

		ContentBlock secondContentBlock = new ContentBlock();
		secondContentBlock.setId("contentBlockId2");
		secondContentBlock.setContentOrder(Integer.valueOf(4));

		List<IContentBlock> contentBlocks = new ArrayList<>();
		contentBlocks.add(firstContentBlock);
		contentBlocks.add(secondContentBlock);
		slide.setContents(contentBlocks);
		when(slideManager.getSlide(slideId)).thenReturn(slide);
		when(slideRepo.findById("notARealId")).thenReturn(Optional.empty());

		List<IContentBlock> contentBlock = managerToTest.getAllContentBlocks(slideId);
		assertTrue(!contentBlock.isEmpty());

	}

	@Test
	public void test_deleteChoiceBlockById_success() throws BlockDoesNotExistException {
		String blockId = "realBlockId";
		String slideId = "realSlideId";
		when(contentBlockRepository.findById(blockId)).thenReturn(Optional.of(contentBlock));
		managerToTest.deleteChoiceBlockById(blockId, slideId);

	}

	@Test
	public void test_deleteChoiceBlockById_nullBlockId() throws BlockDoesNotExistException {
		String blockId = null;
		String slideId = "realSlideId";
		managerToTest.deleteChoiceBlockById(blockId, slideId);

	}

	@Test(expected = BlockDoesNotExistException.class)
	public void test_deleteChoiceBlockById_missingContentBlock() throws BlockDoesNotExistException {
		String blockId = "realBlockId";
		String slideId = "realSlideId";
		when(contentBlockRepository.findById(blockId)).thenReturn(Optional.empty());
		managerToTest.deleteChoiceBlockById(blockId, slideId);

	}

	@Test(expected = BlockDoesNotExistException.class)
	public void test_deleteChoiceBlockById_nonExistentContentBlock() throws BlockDoesNotExistException {
		String blockId = "realBlockId";
		String slideId = "realSlideId";
		when(contentBlockRepository.findById(blockId)).thenReturn(Optional.of(contentBlock));
		Mockito.doThrow(EmptyResultDataAccessException.class).when(choiceBlockRepo).deleteById(blockId);
		managerToTest.deleteChoiceBlockById(blockId, slideId);

	}

	@Test
	public void test_updateImageBlock_success1() throws ImageCouldNotBeStoredException {
		String slideId = "realSlideId";
		String fileName = "dummyFile";
		ISlide slide = new Slide();
		slide.setId(slideId);
		IVSImage slideContentImage = new VSImage();
		slideContentImage.setHeight(700);
		slideContentImage.setWidth(1300);
		slideContentImage.setFilename(fileName);
		byte[] image = new byte[1000];
		IImageBlock imageBlock = new ImageBlock();
		when(slideManager.getSlide(slideId)).thenReturn(slide);
		when(imageFactory.createImage(fileName, fileName)).thenReturn(slideContentImage);
		when(imageRepo.save((VSImage) slideContentImage)).thenReturn((VSImage) slideContentImage);
		managerToTest.updateImageBlock(imageBlock, image, fileName);

	}

	@Test
	public void test_updateImageBlock_success2() throws ImageCouldNotBeStoredException {

		IVSImage slideContentImage = new VSImage();
		slideContentImage.setHeight(700);
		slideContentImage.setWidth(1300);
		IImageBlock imageBlock = new ImageBlock();
		managerToTest.updateImageBlock(imageBlock, slideContentImage);

	}

	@Test(expected = Exception.class)
	public void test_createImageBlock_success() throws ImageCouldNotBeStoredException {
		String slideId = "slide1";
		String fileName = "dummyFile";
		Integer contentOrder = 3;
		ISlide slide = new Slide();
		slide.setId(slideId);
		IVSImage slideContentImage = new VSImage();
		slideContentImage.setHeight(700);
		slideContentImage.setWidth(1300);
		IImageBlock imageBlock = new ImageBlock();
		imageBlock.setContentOrder(contentOrder);
		byte[] image = new byte[1000];

		when(slideManager.getSlide(slideId)).thenReturn(slide);
		when(imageFactory.createImage(fileName, fileName)).thenReturn(slideContentImage);
		when(imageBlockRepo.save((ImageBlock)imageBlock)).thenReturn((ImageBlock)imageBlock);
		Mockito.when(imageBlockFactory.createImageBlock(slide, slideContentImage)).thenReturn(imageBlock);
		managerToTest.createImageBlock(slideId, image, fileName, contentOrder);

	}
	
	@Test
	public void test_saveSpaceBlock_success() throws ImageCouldNotBeStoredException {
		ISpaceBlock spaceBlock = new SpaceBlock();
		spaceBlock.setContentOrder(3);
		spaceBlock.setId("spaceBlock1");
		managerToTest.saveSpaceBlock(spaceBlock);
		Mockito.verify(spaceBlockRepo).save((SpaceBlock)spaceBlock);
        
        
        
		
	}

}
