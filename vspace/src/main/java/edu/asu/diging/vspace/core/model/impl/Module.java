package edu.asu.diging.vspace.core.model.impl;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import edu.asu.diging.vspace.core.model.IModule;
import edu.asu.diging.vspace.core.model.ISequence;
import edu.asu.diging.vspace.core.model.ISlide;

@Entity
public class Module extends VSpaceElement implements IModule {

	@Id 
	@GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", 
      parameters = @Parameter(name = "prefix", value = "MOD"), 
      strategy = "edu.asu.diging.vspace.core.data.IdGenerator")
	private String id;
	
	@OneToMany(mappedBy="module", targetEntity=Slide.class)
	private List<ISlide> slides;

	@OneToMany(mappedBy = "module", targetEntity=Sequence.class)
	private List<ISequence> sequences;
		
	@OneToOne(targetEntity=Sequence.class)
	private ISequence startSequence;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModule#getId()
	 */
	@Override
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IModule#setId(java.lang.String)
	 */
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
	
	@Override
	public List<ISequence> getSequences() {
		return sequences;
	}

	@Override
	public void setSequences(List<ISequence> sequences) {
		this.sequences = sequences;
	}
	
	@Override
	public ISequence getStartSequence() {
		return startSequence;
	}
	
	@Override
	public void setStartSequence(ISequence startSequence) {
		this.startSequence = startSequence;
	}
	
}
