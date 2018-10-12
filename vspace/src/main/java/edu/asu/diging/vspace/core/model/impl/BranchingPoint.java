package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IBranchingPoint;

public class BranchingPoint extends Slide implements IBranchingPoint{
	
	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "BPNT"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToMany(mappedBy = "branchingpoint", targetEntity=Choice.class)
	private List<Choice> choices;
	
	@Override
	public List<Choice> getChoices() {
		return choices;
	}
	
	@Override
	public void setChoices(List<Choice> choices) {
		this.choices = choices;
		
	}
}
