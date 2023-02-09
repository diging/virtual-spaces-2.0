package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.SpaceForm;

@Service
public class SpaceFactory implements ISpaceFactory {


	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.ISpaceFactory#createSpace(edu.asu.diging.vspace.web.staff.forms.SpaceForm)
	 */
	@Override
	public ISpace createSpace(SpaceForm form) {
		ISpace space = new Space();
		space.setName(form.getName());
		space.setDescription(form.getDescription());
		return space;
	}   
}
