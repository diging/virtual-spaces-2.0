package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.IDefaultExhibitionFactory;
import edu.asu.diging.vspace.core.model.IDefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.DefaultExhibition;
import edu.asu.diging.vspace.core.model.impl.Space;
import edu.asu.diging.vspace.web.staff.forms.DefaultExhibitionForm;

@Service
public class DefaultExhibitionFactory implements IDefaultExhibitionFactory {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.ISpaceFactory#createSpace(edu.asu.diging.vspace.web.staff.forms.SpaceForm, java.lang.String)
	 */
	@Override
	public DefaultExhibition createDefaultExhibition(DefaultExhibitionForm defaultExhibitionForm, Space space) {
		DefaultExhibition exhibit = new DefaultExhibition();
		exhibit.setSpace(space);
		return exhibit;
	}
}
