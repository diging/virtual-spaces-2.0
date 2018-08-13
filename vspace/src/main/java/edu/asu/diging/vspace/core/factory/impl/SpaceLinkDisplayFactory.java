package edu.asu.diging.vspace.core.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.vspace.core.factory.ISpaceLinkDisplayFactory;
import edu.asu.diging.vspace.core.model.ISpaceLink;
import edu.asu.diging.vspace.core.model.display.ISpaceLinkDisplay;
import edu.asu.diging.vspace.core.model.display.impl.SpaceLinkDisplay;

@Service
public class SpaceLinkDisplayFactory implements ISpaceLinkDisplayFactory {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.factory.impl.ISpaceLinkDisplayFactory#createSpaceLinkDisplay(edu.asu.diging.vspace.core.model.ISpaceLink)
	 */
	@Override
	public ISpaceLinkDisplay createSpaceLinkDisplay(ISpaceLink link) {
		ISpaceLinkDisplay display = new SpaceLinkDisplay();
		display.setLink(link);
		return display;
	}
}
