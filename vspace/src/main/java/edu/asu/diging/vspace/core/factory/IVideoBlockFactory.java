package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSVideo;
import edu.asu.diging.vspace.core.model.IVideoBlock;

public interface IVideoBlockFactory {

    IVideoBlock createVideoBlock(ISlide slide, IVSVideo video);
}
