package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoiceBlock;

/**
 * Interface for managing choice content blocks, extending the generic content block manager.
 */
public interface IChoiceBlockManager extends IGenericContentBlockManager<IChoiceBlock> {

    /**
     * Creates a new choice block for the specified slide.
     * 
     * @param slideId The ID of the slide
     * @param selectedChoices List of selected choice IDs
     * @param showsAll Whether to show all choices or only selected ones
     * @return The created choice block
     */
    IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, boolean showsAll);
}
