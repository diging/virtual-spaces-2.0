package edu.asu.diging.vspace.core.model;

import java.time.OffsetDateTime;

import edu.asu.diging.vspace.core.model.impl.ShowSpaceLinksToUnpublishedSpaces;
import edu.asu.diging.vspace.core.model.impl.SpaceStatus;

public interface IVSpaceElement {

    String getId();

    void setId(String id);

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

    SpaceStatus getSpaceStatus();

    void setSpaceStatus(SpaceStatus status);

    ShowSpaceLinksToUnpublishedSpaces getShowSpaceLinksToUnpublishedSpaces();

    void setShowSpaceLinksToUnpublishedSpaces(ShowSpaceLinksToUnpublishedSpaces showSpaceLinksToUnpublishedSpaces);

}