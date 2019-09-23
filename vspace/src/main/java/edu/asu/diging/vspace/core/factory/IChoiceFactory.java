package edu.asu.diging.vspace.core.factory;

import java.util.List;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;

public interface IChoiceFactory {

    List<IChoice> createChoices(List<String> choices, IBranchingPoint branchingPoint);

}
