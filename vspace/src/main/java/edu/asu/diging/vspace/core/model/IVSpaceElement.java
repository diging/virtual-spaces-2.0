package edu.asu.diging.vspace.core.model;

import java.time.OffsetDateTime;

public interface IVSpaceElement {

	String getName();

	void setName(String name);

	String getDescription();

	void setDescription(String description);

	String getCreatedBy();

	void setCreatedBy(String createdBy);

	OffsetDateTime getCreationDate();

	void setCreationDate(OffsetDateTime creationDate);

	String getModifiedBy();

	void setModifiedBy(String modifiedBy);

	OffsetDateTime getModificationDate();

	void setModificationDate(OffsetDateTime modificationDate);

}