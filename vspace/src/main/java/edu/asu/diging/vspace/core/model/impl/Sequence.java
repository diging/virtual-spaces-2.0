package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;

public class Sequence extends VSpaceElement implements ISequence{

	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "SEQ"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToMany(mappedBy = "sequence", targetEntity=Slide.class)
	private List<ISlide> slides;
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public List<ISlide> getSlides() {
		return slides;
	}
	
	@Override
	public void setSlides(List<ISlide> slides) {
		this.slides = slides;
	}
}

