package edu.asu.diging.vspace.references;

import edu.asu.diging.vspace.core.model.impl.Reference;

public interface ReferenceMetadataProvider {
    
    public ReferenceMetaDataType getReferenceMetadataType();
    
    public String getReferenceMetadata(Reference reference);
    
}
