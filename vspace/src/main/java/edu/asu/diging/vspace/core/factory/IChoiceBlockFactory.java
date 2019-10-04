package edu.asu.diging.vspace.core.factory;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISlide;

public interface IChoiceBlockFactory {

    IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder, IChoice choice);

}
