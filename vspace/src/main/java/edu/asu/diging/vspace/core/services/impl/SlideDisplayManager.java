package edu.asu.diging.vspace.core.services.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.display.SlideDisplayRepository;
import edu.asu.diging.vspace.core.data.display.SpaceDisplayRepository;
import edu.asu.diging.vspace.core.factory.ISlideDisplayFactory;
import edu.asu.diging.vspace.core.factory.ISpaceDisplayFactory;
import edu.asu.diging.vspace.core.file.IStorageEngine;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.display.ISlideDisplay;
import edu.asu.diging.vspace.core.model.display.ISpaceDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SlideDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceDisplay;
import edu.asu.diging.vspace.core.services.IImageService;
import edu.asu.diging.vspace.core.services.ISlideDisplayManager;
import edu.asu.diging.vspace.core.services.ISpaceDisplayManager;
import edu.asu.diging.vspace.core.services.impl.model.ImageData;

@Service
@PropertySource("classpath:config.properties")
public class SlideDisplayManager implements ISlideDisplayManager{
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${bg_image_max_width}")
    private int maxBgImageWidth;

    @Value("${bg_image_max_height}")
    private int maxBgImageHeight;

    @Autowired
    private SlideDisplayRepository slideDisplayRepo;

    @Autowired
    private ISlideDisplayFactory displayFactory;

    @Autowired
    private IImageService imageService;

    @Autowired
    private IStorageEngine storage;
    
    @Override
    public ISlideDisplay getBySlide(ISlide slide) {
        IVSImage image = slide!=null ? slide.getImage():null;
        List<SlideDisplay> displays = slideDisplayRepo.getBySlide(slide);
        ISlideDisplay display = displays.isEmpty() ? null:displays.get(0);
        if(display==null) {
            display = displayFactory.createSlideDisplay();
        }
     
        if(image!=null) {
            if(image.getWidth()<=0 || image.getHeight()<=0) {
                try {
                    ImageData data = imageService.getImageData(storage.getMediaContent(image.getId(), image.getFilename()));
                    if (data != null) {
                        image.setWidth(data.getWidth());
                        image.setHeight(data.getHeight());
                    }
                    
                } catch (IOException e) {
                    logger.error("Could not get image.", e);
                    return display;
                }
            }

            if(image.getWidth()<=maxBgImageWidth && image.getHeight()<=maxBgImageHeight) {
                display.setWidth(image.getWidth());
                display.setHeight(image.getHeight());
                return display;
            }

            ImageData data = imageService.getImageDimensions(image, maxBgImageWidth, maxBgImageHeight);
            display.setHeight(data.getHeight());
            display.setWidth(data.getWidth());
            slideDisplayRepo.save((SlideDisplay) display);
        }

        return display;
    }

}
