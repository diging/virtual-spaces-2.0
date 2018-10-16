package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IBranchingPoint;
import edu.asu.diging.vspace.core.model.IChoice;

@Entity
public class BranchingPoint extends Slide implements IBranchingPoint{
	
	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "branchingpoint-id-generator", 	
					parameters = @Parameter(name = "prefix", value = "BPNT"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToMany(mappedBy = "branchingpoint", targetEntity=Choice.class)
	private List<IChoice> choices;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IBranchingPoint#getChoices()
	 */
	@Override
	public List<IChoice> getChoices() {
		return choices;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IBranchingPoint#setChoices()
	 */
	@Override
	public void setChoices(List<IChoice> choices) {
		this.choices = choices;		
	}
	
}
