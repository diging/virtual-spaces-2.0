package edu.asu.diging.vspace.core.services;


import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

import java.util.List;

public interface IImageService {

    ImageData getImageData(byte[] image);

    ImageData getImageDimensions(IVSImage image, int width, int height);
    
    long getTotalPages();
    
    List<VSImage> getImages(int pageNo);
    
    long getTotalImageCount();

    int validatePageNumber(int pageNo);
    
    void editImage(String imageId, ImageForm imageForm) throws ImageDoesNotExistException;
    
    IVSImage getImageById(String imageId);

}