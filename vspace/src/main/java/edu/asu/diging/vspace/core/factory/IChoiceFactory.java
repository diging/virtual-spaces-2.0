package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoice;

/*
* (non-Javadoc)
* The {@code IChoiceFactory} interface defines a factory for creating a list of choices.
* It provides a single method for creating a list of {@link IChoice} objects from a list of java.lang.String.
* 
*/
public interface IChoiceFactory {

    /* 
     * @see
     * edu.asu.diging.vspace.core.factory.impl.ChoiceFactory#createChoices(java.util.List<java.lang.String>)
     */
    List<IChoice> createChoices(List<String> choices);

}
