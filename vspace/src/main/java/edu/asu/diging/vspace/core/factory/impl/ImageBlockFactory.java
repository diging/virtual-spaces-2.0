package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IImageBlockFactory;
import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;
import edu.asu.diging.vspace.core.model.impl.ImageBlock;

@Service
public class ImageBlockFactory implements IImageBlockFactory {

    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.IImageBlockFactory#createImageBlock(
     * edu.asu.diging.vspace.core.model.ISlide,
     * edu.asu.diging.vspace.core.model.IVSImage)
     */
    @Override
    public IContentBlock createImageBlock(ISlide slide, IVSImage image) {
        IContentBlock imageblock = new ImageBlock();
        ((IImageBlock) imageblock).setImage(image);
        imageblock.setSlide(slide);
        imageblock.setDescription("image");

        return (IContentBlock) imageblock;
    }
}
