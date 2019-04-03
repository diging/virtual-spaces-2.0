package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IContentBlock;
import edu.asu.diging.vspace.core.model.ISlide;

public interface ITextBlockFactory {

    IContentBlock createTextBlock(ISlide slide, String text);

}
