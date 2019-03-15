package edu.asu.diging.vspace.core.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.data.ContentBlockRepository;
import edu.asu.diging.vspace.core.data.TextContentBlockRepository;
import edu.asu.diging.vspace.core.factory.ITextBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;
import edu.asu.diging.vspace.core.model.impl.ContentBlock;
import edu.asu.diging.vspace.core.model.impl.Slide;
import edu.asu.diging.vspace.core.services.IContentBlockManager;

@Service
public class ContentBlockManager implements IContentBlockManager{
    
    @Autowired
    private ITextBlockFactory textBlockFactory;
    
    @Autowired
    private SlideManager slideManager;
    
    @Autowired
    private TextContentBlockRepository textBlockRepo;
    
    @Autowired
    private ContentBlockRepository contentBlockRepo;

//    @Override
//    public List<IContentBlock> getAllContentBlocks(String slideId) {
//        IModule slide = getSlide(slideId);
//        return new ArrayList<>(slideRepo.findBySlide((Slide) slide));
//    }
    
    //getAllImageContentBlocks
    
    //getAllTextContentBlocks
    
    @Override
    public IContentBlock createTextBlock(String slideId, String text) {
        System.out.println("Inside Manager");
        IContentBlock textBlock = textBlockFactory.createTextBlock(slideId,text);
        System.out.println(textBlock.getSlide().getId());
        storeTextBlock(textBlock);
        return textBlock;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IContentBlock#storeTextBlock(edu.asu.
     * diging.vspace.core.model.ITextBlock)
     */
    @Override
    public ITextBlock storeTextBlock(IContentBlock textBlock) {
        return textBlockRepo.save((ITextBlock) textBlock);
        
    }

    @Override
    public void createImageBlock(String slideId, String value) {
        // TODO Auto-generated method stub
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.services.impl.IModuleManager#storeSlide(edu.asu.
     * diging.vspace.core.model.ISlide, java.lang.String)
     */
//    public CreationReturnValue storeSlide(ISlide slide, byte[] image, String filename) {
//        IVSImage slideImage = null;
//        if (image != null && image.length > 0) {
//            Tika tika = new Tika();
//            String contentType = tika.detect(image);
//
//            slideImage = imageFactory.createImage(filename, contentType);
//            slideImage = imageRepo.save((VSImage) slideImage);
//        }
//
//        CreationReturnValue returnValue = new CreationReturnValue();
//        returnValue.setErrorMsgs(new ArrayList<>());
//
//        if (slideImage != null) {
//            String relativePath = null;
//            try {
//                relativePath = storage.storeFile(image, filename, slideImage.getId());
//            } catch (FileStorageException e) {
//                returnValue.getErrorMsgs().add("Background image could not be stored: " + e.getMessage());
//            }
//            slideImage.setParentPath(relativePath);
//            imageRepo.save((VSImage) slideImage);
//            slide.setImage(slideImage);
//        }
//
//        slide = slideRepo.save((Slide) slide);
//        returnValue.setElement(slide);
//        return returnValue;
//    }
}
