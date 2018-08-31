package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDefaultExhibition extends IVSpaceElement {

	Space getSpace();

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
	 */
	void setSpace(Space space);

}