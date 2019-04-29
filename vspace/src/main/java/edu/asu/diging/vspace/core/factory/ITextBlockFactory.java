package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ITextBlock;

public interface ITextBlockFactory {

    ITextBlock createTextBlock(ISlide slide, String text);

}
