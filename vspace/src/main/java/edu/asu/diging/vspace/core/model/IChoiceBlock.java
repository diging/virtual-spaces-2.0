package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IChoiceBlock extends IContentBlock {

    List<IChoice> getChoices();
    
    void setChoices(List<IChoice> choices);

    void setShowsAll(boolean showsAll);

    boolean isShowsAll();
    
}
