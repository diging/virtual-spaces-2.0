package edu.asu.diging.vspace.references;

import edu.asu.diging.vspace.core.model.IReference;

public interface ReferenceMetadataProvider {
    public ReferenceMetaDataType getReferenceMetadataType();
    public String getReferenceMetadata(IReference reference);
}
