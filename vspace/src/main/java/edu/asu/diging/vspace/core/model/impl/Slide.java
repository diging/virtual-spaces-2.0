package edu.asu.diging.vspace.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.ISlide;
import edu.asu.diging.vspace.core.model.IVSImage;

@Entity
public class Slide extends VSpaceElement implements ISlide {
	
	@Id
	@GeneratedValue(generator = "id-generator")
	@GenericGenerator(name = "slide-id-generator", 	
	  parameters = @Parameter(name = "prefix", value = "SLI"),
	  strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	@OneToOne(targetEntity=VSImage.class)
	private IVSImage image;

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISlide#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISlide#setId(java.lang.String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISlide#getImage()
	 */
	@Override
	public IVSImage getImage() {
		return image;
	}

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISlide#setImage(edu.asu.diging.vspace.core.model.IVSImage)
	 */
	@Override
	public void setImage(IVSImage image) {
		this.image = image;
	}
	
}
