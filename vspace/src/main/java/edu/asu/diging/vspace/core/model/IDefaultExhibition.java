package edu.asu.diging.vspace.core.model;

import edu.asu.diging.vspace.core.model.impl.Space;

public interface IDefaultExhibition {

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getId()
	 */
	String getId();

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setId(java.lang.String)
	 */
	void setId(String id);

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#getSpaceLinks()
	 */
	Space getSpace();

	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.ISpacee#setSpaceLinks(java.util.List)
	 */
	void setSpace(Space space);

}