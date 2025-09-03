package edu.asu.diging.vspace.core.services;

import java.util.List;

import edu.asu.diging.vspace.core.model.IChoiceBlock;

public interface IChoiceBlockManager extends IGenericContentBlockManager<IChoiceBlock> {

    IChoiceBlock createChoiceBlock(String slideId, List<String> selectedChoices, boolean showsAll);
}
