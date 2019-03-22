package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

public interface IImageBlockFactory {

    IContentBlock createImageBlock(ISlide slide, IVSImage image);

}
