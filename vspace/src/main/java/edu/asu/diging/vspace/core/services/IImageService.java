package edu.asu.diging.vspace.core.services;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public interface IImageService {

	ImageData getImageData(byte[] image);

	ImageData getImageDimensions(IVSImage image, int width, int height);

}