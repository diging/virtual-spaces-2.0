package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceBlock;

public interface ISpaceBlockFactory {

    ISpaceBlock createSpaceBlock(ISlide slide, String text, ISpace space);
}
