package edu.asu.diging.vspace.core.services.impl;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageContentBlockRepository;
import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Transactional
@Service
public class ContentBlockManager implements IContentBlockManager {

	@Autowired
	private ITextBlockFactory textBlockFactory;

	@Autowired
	private IImageBlockFactory imageBlockFactory;

	@Autowired
	private SlideManager slideManager;

	@Autowired
	private TextContentBlockRepository textBlockRepo;

	@Autowired
	private ImageContentBlockRepository imageBlockRepo;

	@Autowired
	private IImageFactory imageFactory;

	@Autowired
	private ImageRepository imageRepo;

	@Autowired
	private IStorageEngine storage;

//	@Autowired
//	private ContentBlockRepository contentBlockRepo;

	// @Override
	// public List<IContentBlock> getAllContentBlocks(String slideId) {
	// IModule slide = getSlide(slideId);
	// return new ArrayList<>(slideRepo.findBySlide((Slide) slide));
	// }

	// getAllImageContentBlocks

	// getAllTextContentBlocks

	@Override
	public IContentBlock createTextBlock(String slideId, String text) {
		System.out.println("Inside Manager");
		IContentBlock textBlock = textBlockFactory.createTextBlock(slideId, text);
		// System.out.println(textBlock.getSlide().getId());
		storeTextBlock(textBlock);
		return textBlock;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.diging.vspace.core.services.impl.ITextBlock#storeTextBlock(edu.
	 * asu.diging.vspace.core.model.IContentBlock)
	 */
	@Override
	public ITextBlock storeTextBlock(IContentBlock textBlock) {
		return textBlockRepo.save((ITextBlock) textBlock);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.services.impl.IImageBlock#storeImageBlock(edu.
	 * asu.diging.vspace.core.model.IContentBlock)
	 */
	@Override
	public IImageBlock storeImageBlock(IContentBlock imageBlock) {
		return imageBlockRepo.save((IImageBlock) imageBlock);

	}

	@Override
	public CreationReturnValue createImageBlock(String slideId, byte[] image, String filename) {
		// TODO Auto-generated method stub
		IVSImage slideContentImage = null;
		ISlide slide = slideManager.getSlide(slideId);
		if (image != null && image.length > 0) {
			Tika tika = new Tika();
			String contentType = tika.detect(image);

			slideContentImage = imageFactory.createImage(filename, contentType);
			slideContentImage = imageRepo.save((VSImage) slideContentImage);
		}

		CreationReturnValue returnValue = new CreationReturnValue();
		returnValue.setErrorMsgs(new ArrayList<>());

		if (slideContentImage != null) {
			String relativePath = null;
			try {
				relativePath = storage.storeFile(image, filename, slideContentImage.getId());
			} catch (FileStorageException e) {
				returnValue.getErrorMsgs().add("Image could not be stored: " + e.getMessage());
			}
			slideContentImage.setParentPath(relativePath);
			imageRepo.save((VSImage) slideContentImage);
		}

		IContentBlock imgBlock = imageBlockFactory.createImageBlock(slide, slideContentImage);
		ImageBlock imageBlock = (ImageBlock) storeImageBlock(imgBlock);

		returnValue.setElement(imageBlock);
		return returnValue;
	}

}
