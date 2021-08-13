package edu.asu.diging.vspace.references;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;

public interface ReferenceMetadataProvider {
    
    public ReferenceMetadataType getReferenceMetadataType();
    
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException;
    
}
