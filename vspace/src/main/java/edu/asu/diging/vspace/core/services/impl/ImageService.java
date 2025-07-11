package edu.asu.diging.vspace.core.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ImageRepository;
import edu.asu.diging.vspace.core.exception.FileStorageException;
import edu.asu.diging.vspace.core.exception.ImageDoesNotExistException;
import edu.asu.diging.vspace.core.factory.IImageFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.ImageCategory;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;
import edu.asu.diging.vspace.web.staff.forms.ImageForm;

@Service
public class ImageService implements IImageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private IImageFactory imageFactory;

    @Autowired
    private IStorageEngine storage;

    @Value("${page_size}")
    private int pageSize;

    private final String DEFAULT_IMAGE_EXCEPTION = "Default image could not be stored: ";
    private final String NO_IMAGE_EXCEPTION = "Image doesn't exist for image id";

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IImageService#getImageData(byte[])
     */
    @Override
    public ImageData getImageData(byte[] image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            if(bufferedImage!=null) {
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
     * Method to calculate the biggest possible dimensions of an image given a
     * bounding box.
     * 
     * @param image  The image to calculate new dimensions for.
     * @param width  width of bounding box
     * @param height height of bounding box
     * @return new image dimensions
     */
    @Override
    public ImageData getImageDimensions(IVSImage image, int width, int height) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Float newWidth = (new Float(height) / new Float(imageHeight)) * new Float(imageWidth);
        if(newWidth<width) {
            return new ImageData(height, newWidth.intValue());
        }

        Float newHeight = (new Float(width) / new Float(imageWidth)) * new Float(imageHeight);
        return new ImageData(newHeight.intValue(), width);
    }

    private Sort getSortingParameters(String sortedBy, String order) {
        Sort sortingParameters = Sort.by(SortByField.CREATION_DATE.getValue()).descending();
        if(sortedBy!=null && SortByField.getAllValues().contains(sortedBy)) {
            sortingParameters = Sort.by(sortedBy);
        }
        if(order!=null && order.equalsIgnoreCase(Sort.Direction.ASC.toString())) {
            sortingParameters = sortingParameters.ascending();
        } else {
            sortingParameters = sortingParameters.descending();
        }
        return sortingParameters;
    }
    
    /**
     * Method to return the requested images
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @param category. if category value is not null image list is filtered using 
     *                category value
     * @return list of images for the requested pageNo and category
     */
    @Override
    public List<IVSImage> getImages(int pageNo, ImageCategory category) {
        return getImages(pageNo, category, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
    }
     
    @Override
    public List<IVSImage> getImages(int pageNo) {
        return getImages(pageNo, null, SortByField.CREATION_DATE.getValue(), Sort.Direction.DESC.toString());
    }
    
    /**
     * Method to return the requested images
     * 
     * @param pageNo. if pageNo<1, 1st page is returned, if pageNo>total pages,last
     *                page is returned
     * @return list of images in the requested pageNo and requested order.
     */

    @Override
    public List<IVSImage> getImages(int pageNo, ImageCategory category, String sortedBy, String order) {
        Sort sortingParameters = getSortingParameters(sortedBy, order);
        pageNo = validatePageNumber(pageNo, category);
        Pageable sortByRequestedField = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        Page<VSImage> images;
        if(category==null) {
            images = imageRepo.findAll(sortByRequestedField);
        } else {
            images = imageRepo.findByCategories(sortByRequestedField, category);
        }
        List<IVSImage> results = new ArrayList<>();
        if(images != null) {
            images.getContent().forEach(i -> results.add(i));
        }
        return results;
    }
    
    /**
     * Method to return the total image count
     * 
     * @return total count of images in DB
     */

    @Override
    public long getTotalImageCount(ImageCategory category) {
        if(category==null) {
            return imageRepo.count();
        }
        return imageRepo.countByCategories(category);
    }
    
    /**
     * Method to return the total pages sufficient to display all images
     * 
     * @return totalPages required to display all images in DB
     */

    
    @Override
    public long getTotalPages(ImageCategory category) {
        if(category==null) {
            return (imageRepo.count() % pageSize==0) ? imageRepo.count() / pageSize:(imageRepo.count() / pageSize) + 1;
        }
        long count = imageRepo.countByCategories(category);
        return (count%pageSize==0) ? count/pageSize : (count/pageSize)+1;
    }
    
    /**
     * Method to return page number after validation
     * 
     * @param pageNo page provided by calling method
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than
     *         totalPages.
     */

    @Override
    public int validatePageNumber(int pageNo, ImageCategory category) {
        long totalPages = getTotalPages(category);
        if(pageNo<1) {
            return 1;
        } else if(pageNo>totalPages) {
            return (totalPages==0) ? 1:(int) totalPages;
        }
        return pageNo;
    }

    /**
     * Method to edit image details
     * 
     * @param imageId   - image unique identifier
     * @param imageForm - ImageForm with updated values for image fields
     * @return throws ImageDoesNotExistException if no image exists with id,
     */
    @Override
    public void editImage(String imageId, ImageForm imageForm) throws ImageDoesNotExistException {
        IVSImage image = getImageById(imageId);
        image.setName(imageForm.getName());
        image.setDescription(imageForm.getDescription());
        imageRepo.save((VSImage) image);
    }

    /**
     * Method to lookup image by id
     * 
     * @param imageId - image unique identifier
     * @return image with provided image id if it exists, throws
     *         ImageDoesNotExistException if no image exists with id,
     */
    @Override
    public IVSImage getImageById(String imageId) throws ImageDoesNotExistException {
        Optional<VSImage> imageOptional = imageRepo.findById(imageId);
        if(imageOptional.isPresent()) {
            return imageOptional.get();
        } else {
            throw new ImageDoesNotExistException(NO_IMAGE_EXCEPTION + imageId);
        }
    }

    @Override
    public List<IVSImage> findByFilenameOrNameContains(String searchTerm) {
        String likeSearchTerm = "%" + searchTerm + "%";
        List<VSImage> results = imageRepo.findByFilenameLikeOrNameLike(likeSearchTerm, likeSearchTerm);
        List<IVSImage> imageResults = new ArrayList<>();
        results.forEach(r -> imageResults.add(r));
        return imageResults;
    }

    @Override
    public void addCategory(IVSImage image, ImageCategory category) {
        if(image.getCategories()==null) {
            image.setCategories(new ArrayList<>());
        }

        if(!image.getCategories().contains(category)) {
            image.getCategories().add(category);
        }
        imageRepo.save((VSImage) image);
    }

    @Override
    public void removeCategory(IVSImage image, ImageCategory category) {
        image.getCategories().remove(category);
        imageRepo.save((VSImage) image);
    }   
    
    /**
     * Method to store an image in the file 
     * 
     *@param image - The image data as a byte array
     *@param filename - The name of the file to be stored
     *@return {@link IVSImage} instance
     */
    @Override
    public IVSImage storeImage(byte[] image, String filename) {

        IVSImage storedImage = null;
        if (image != null && image.length > 0) {
            Tika tika = new Tika();
            String contentType = tika.detect(image);
            storedImage = imageFactory.createImage(filename, contentType);
            storedImage = imageRepo.save((VSImage) storedImage);
        }

        CreationReturnValue returnValue = new CreationReturnValue();
        returnValue.setErrorMsgs(new ArrayList<>());
        if (storedImage != null) {
            String relativePath = null;
            try {
                relativePath = storage.storeFile(image, filename, storedImage.getId());
            } catch (FileStorageException e) {
                // don't store a VSImage instance, if an exception occurs
                imageRepo.deleteById(storedImage.getId());
                
                logger.error(DEFAULT_IMAGE_EXCEPTION,e);
                returnValue.getErrorMsgs().add(DEFAULT_IMAGE_EXCEPTION + e.getMessage());
            }
            storedImage.setParentPath(relativePath);
            ImageData imageData = getImageData(image);

            if (imageData != null) {
                storedImage.setHeight(imageData.getHeight());
                storedImage.setWidth(imageData.getWidth());
            }
            imageRepo.save((VSImage) storedImage);
        }
        return storedImage;
    }

    /**
     *Method to return content of the image
     *
     *@param image - {@link IVSImage} instance of the image to be retrieved
     *@return byte[] - the image content as byte array
     */
    @Override
    public byte[] getImageContent(IVSImage image) {
        try {
            return storage.getMediaContent(image.getId(), image.getFilename());
        } catch (IOException e) {
            logger.error("Could not retrieve the image",e);  
        }
        return null;
    }
    
    /**
     * Retrieves a list of IVSImages for pagination based on the provided parameters.
     * 
     * @param pageNo.    if pageNo<1, 1st page is returned, if pageNo>total
     *                   pages,last page is returned
     * @param searchTerm - This is the search string which is being searched.
     * @param category - The category of the images to filter by.
     * @return list of images in the requested pageNo and requested order.
     */
    @Override
    public Page<VSImage> getPaginatedImagesBySearchTerm(int pageNo, ImageCategory category, String searchTerm,
            String sortedBy, String order) {    	
    	Sort sortingParameters = getSortingParameters(sortedBy, order);
        pageNo = validatePageNumber(pageNo, category);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sortingParameters);
        String likeSearchTerm = "%" + searchTerm + "%";
        Page<VSImage> results;
        if(category!=null) {
            results = imageRepo.findByCategoryAndFilenameLikeOrNameLikeOrDescriptionLike(
                    pageable, category, likeSearchTerm, likeSearchTerm, likeSearchTerm);
        }
        else {
            results = imageRepo.findByFilenameLikeOrNameLikeOrDescriptionLike(pageable,
                    likeSearchTerm, likeSearchTerm, likeSearchTerm);
        }
        return results;
    }
}
