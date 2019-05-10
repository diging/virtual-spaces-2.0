package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IImageBlock;
import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

public interface IImageBlockFactory {

    IImageBlock createImageBlock(ISlide slide, IVSImage image);

}
