package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISlide;

public interface IChoiceBlockFactory {

    IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder, List<IChoice> choices, boolean showsAll);

}
