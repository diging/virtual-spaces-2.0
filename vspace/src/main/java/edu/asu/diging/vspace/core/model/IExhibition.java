package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.Space;

public interface IExhibition extends IVSpaceElement {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.model.impl.ISpacee#getSpaceLinks(java.util.List)
	 */
	Space getSpace();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
	 */
	void setSpace(Space space);
}