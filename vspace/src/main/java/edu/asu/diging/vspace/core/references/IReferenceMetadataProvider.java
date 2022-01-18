package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;

/**
 * This interface is extended by different Metadata Provider classes and implements the methods to create the metadata required for a reference
 *
 */
public interface IReferenceMetadataProvider {
    
    public ReferenceMetadataType getReferenceMetadataType();
    
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException;
    
}
