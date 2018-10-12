package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IChoice;
import edu.asu.diging.vspace.core.model.ISequence;

public class Choice extends VSpaceElement implements IChoice{
	
	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "CHO"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToOne(targetEntity=Sequence.class)
	private ISequence link;
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public ISequence getLink() {
		return link;
	}
	
	@Override
	public void setLink(ISequence link) {
		this.link = link;
	}
	
	
	

}
