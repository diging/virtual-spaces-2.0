package edu.asu.diging.vspace.core.model;

import java.util.List;

public interface IBranchingPoint {

	List<IChoice> getChoices();
	
	void setChoices(List<IChoice> choices);
	
}
