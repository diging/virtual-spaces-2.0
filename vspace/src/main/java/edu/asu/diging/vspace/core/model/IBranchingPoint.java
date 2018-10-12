package edu.asu.diging.vspace.core.model;

import java.util.List;

import edu.asu.diging.vspace.core.model.impl.Choice;

public interface IBranchingPoint {

	List<Choice> getChoices();
	
	void setChoices(List<Choice> choices);
}
