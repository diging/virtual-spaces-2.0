package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.IChoiceBlock;
import edu.asu.diging.vspace.core.model.ISlide;

/*
 * (non-javadoc)
 * The {@code IChoiceBlockFactory} interface defines a factory for creating a choice block.
 * It provides a single method for creating a edu.asu.diging.vspace.core.model.IChoiceBlock
 * object with specified parameters.
 * 
 */
public interface IChoiceBlockFactory {

    /*
     * (non-javadoc)
     * 
     * @param slide the slide to which the choice block belongs
     * @param contentOrder the content order of the choice block within the slide
     * @param choices the list of choices for the block
     * @param showsAll whether to show all choices at once or reveal them gradually
     * @return edu.asu.diging.vspace.core.model.IChoiceBlock object with the specified parameters
     */
    IChoiceBlock createChoiceBlock(ISlide slide, Integer contentOrder, List<IChoice> choices, boolean showsAll);

}
