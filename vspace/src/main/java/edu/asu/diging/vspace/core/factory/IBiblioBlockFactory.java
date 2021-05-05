package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IBiblioBlock;
import edu.asu.diging.vspace.core.model.ISlide;

public interface IBiblioBlockFactory {

    IBiblioBlock createBiblioBlock(ISlide slide, IBiblioBlock biblioBlockData);

}
