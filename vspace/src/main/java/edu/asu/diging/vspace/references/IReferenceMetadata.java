package edu.asu.diging.vspace.references;

import edu.asu.diging.vspace.core.model.IReference;

public interface IReferenceMetadata {
    public String getReferenceMetadataStyle();
    public String createReferenceMetadata(ICitationStyle citationStyle, IReference reference);
}
