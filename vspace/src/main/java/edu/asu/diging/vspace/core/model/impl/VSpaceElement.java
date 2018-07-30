package edu.asu.diging.vspace.core.model.impl;

import java.time.OffsetDateTime;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import edu.asu.diging.vspace.core.model.IVSpaceElement;

@MappedSuperclass
public class VSpaceElement implements IVSpaceElement {

	private String name;
	@Lob private String description;
	private String createdBy;
	private OffsetDateTime creationDate;
	private String modifiedBy;
	private OffsetDateTime modificationDate;
	
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getCreatedBy()
	 */
	@Override
	public String getCreatedBy() {
		return createdBy;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setCreatedBy(java.lang.String)
	 */
	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getCreationDate()
	 */
	@Override
	public OffsetDateTime getCreationDate() {
		return creationDate;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setCreationDate(java.time.OffsetDateTime)
	 */
	@Override
	public void setCreationDate(OffsetDateTime creationDate) {
		this.creationDate = creationDate;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getModifiedBy()
	 */
	@Override
	public String getModifiedBy() {
		return modifiedBy;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setModifiedBy(java.lang.String)
	 */
	@Override
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#getModificationDate()
	 */
	@Override
	public OffsetDateTime getModificationDate() {
		return modificationDate;
	}
	/* (non-Javadoc)
	 * @see edu.asu.diging.vspace.core.model.impl.IVSpaceElement#setModificationDate(java.time.OffsetDateTime)
	 */
	@Override
	public void setModificationDate(OffsetDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}
}
