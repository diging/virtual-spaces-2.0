package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceLinkFactory;
import edu.asu.diging.vspace.core.model.ISpace;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.impl.SpaceLink;

@Service
public class SpaceLinkFactory implements ISpaceLinkFactory {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.ISpaceLinkFactory#createSpaceLink(java.lang.String, edu.asu.diging.vspace.core.model.ISpace)
	 */
	@Override
	public ISpaceLink createSpaceLink(String title, ISpace source) {
		ISpaceLink link = new SpaceLink();
		link.setName(title);
		link.setSourceSpace(source);
		return link;
	}
}
