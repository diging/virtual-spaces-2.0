package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.Space;

public interface IExhibition extends IVSpaceElement {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpace()
	 */
	Space getSpace();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.asu.diging.vspace.core.model.impl.ISpacee#setSpace(edu.asu.diging.vspace.
	 * core.model.impl.Space)
	 */
	void setSpace(Space space);
}