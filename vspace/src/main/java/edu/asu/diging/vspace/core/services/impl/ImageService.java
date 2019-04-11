package edu.asu.diging.vspace.core.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

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
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.SortByField;
import edu.asu.diging.vspace.core.model.impl.VSImage;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Service
public class ImageService implements IImageService {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ImageRepository imageRepo;
    
    @Value("${page_size}")
    private int pageSize;
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
        if (newWidth < width) {
            return new ImageData(height, newWidth.intValue());
        }

        Float newHeight = (new Float(width) / new Float(imageWidth)) * new Float(imageHeight);
        return new ImageData(newHeight.intValue(), width);
    }
    
    /**
     * Method to return the requested images  
     * 
     * @param pageNo. if pageNo<1, 1st page is return, if pageNo>total pages,last page is return
     * @return list of images in the requested pageNo
     */ 
    @Override
    public List<VSImage> getImages(int pageNo) {
        pageNo = validatePageNumber(pageNo);
        Pageable sortByRequestedField = PageRequest.of(pageNo-1, pageSize, Sort.by(SortByField.CREATION_DATE.getValue()));
        Page<VSImage> images = imageRepo.findAll(sortByRequestedField);
        return images.getContent();   
    }  
    
    /**
     * Method to return the total pages sufficient to display all images  
     * 
     * @return totalPages required to display all images in DB
     */ 
    @Override
    public long getTotalPages() {
        return (imageRepo.count() % pageSize == 0) ? imageRepo.count() / pageSize : (imageRepo.count() / pageSize) + 1;
    }
    
    /**
     * Method to return the total image count  
     * 
     * @return total count of images in DB
     */ 
    @Override
    public long getTotalImageCount() {
        return imageRepo.count();
    }
    
    /**
     * Method to return page number after validation   
     * 
     * @param pageNo page provided by calling method
     * @return 1 if pageNo less than 1 and lastPage if pageNo greater than totalPages. 
     */ 
    @Override
    public int validatePageNumber(int pageNo) {
        long totalPages = getTotalPages();
        if(pageNo<1) {
            return 1;
        } else if(pageNo>totalPages) {
            return (totalPages == 0)? 1:(int) totalPages;
        }
        return pageNo;
    }
}
