package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

public class Slide extends VSpaceElement implements ISlide {
	
	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "id-generator", 	
					parameters = @Parameter(name = "prefix", value = "SLI"), 
					strategy = "edu.asu.diging.vspace.core.data.IdGenerator"
			)
	private String id;
	
	@OneToOne(targetEntity=VSImage.class)
	private IVSImage image;

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public IVSImage getImage() {
		return image;
	}

	public void setImage(IVSImage image) {
		this.image = image;
	}
}
