package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IBranchingPoint extends ISlide {

    List<IChoice> getChoices();

    void setChoices(List<IChoice> choices);
}
