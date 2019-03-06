package edu.asu.diging.vspace.core.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Service
public class ImageService implements IImageService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.services.impl.IImageService#getImageData(byte[])
	 */
	@Override
	public ImageData getImageData(byte[] image) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
			if (bufferedImage != null) {
				int imageHeight = bufferedImage.getHeight();
				int imageWidth = bufferedImage.getWidth();
				return new ImageData(imageHeight, imageWidth);
			}
        } catch (IOException e) {
			logger.error("Clould not get image data.", e);
		}
		return null;
	}
	
	/**
	 * Method to calculate the biggest possible dimensions of an image 
	 * given a bounding box. 
	 * @param image The image to calculate new dimensions for.
	 * @param width width of bounding box
	 * @param height height of bounding box
	 * @return new image dimensions
	 */
	@Override
	public ImageData getImageDimensions(IVSImage image, int width, int height) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		Float newWidth = (new Float(height)/new Float(imageHeight))*new Float(imageWidth);
		if (newWidth < width) {
			return new ImageData(height, newWidth.intValue());
		}
		
		Float newHeight= (new Float(width)/new Float(imageWidth)) * new Float(imageHeight);
		return new ImageData(newHeight.intValue(), width);
	}
}
