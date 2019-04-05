package edu.asu.diging.vspace.core.services;

import java.util.Map;

import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

public interface IImageService {

    ImageData getImageData(byte[] image);

    ImageData getImageDimensions(IVSImage image, int width, int height);
    
    Map<String,Object> getImageListingAttr(int currPage);

}