package edu.asu.diging.vspace.core.services;


import java.util.List;

import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

public interface IImageService {

    ImageData getImageData(byte[] image);

    ImageData getImageDimensions(IVSImage image, int width, int height);

    List<IVSImage> getImages(int pageNo, String filter);

    List<IVSImage> getImages(int pageNo, String filter, String sortedBy, String order);

    long getTotalImageCount(String filter);

    long getTotalPages(String filter);

    int validatePageNumber(int pageNo, String filter);

    void editImage(String imageId, ImageForm imageForm) throws ImageDoesNotExistException;

    IVSImage getImageById(String imageId) throws ImageDoesNotExistException;

    void addCategory(IVSImage image, ImageCategory category);

    void removeCategory(IVSImage image, ImageCategory category);

    List<IVSImage> findByFilenameOrNameContains(String searchTerm);

}