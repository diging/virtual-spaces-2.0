package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException;
import edu.asu.diging.vspace.core.model.impl.Reference;


public interface IReferenceMetadataProvider {
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.impl.ReferenceMetadataProvider#getReferenceMetadataType
     * ()
     */
    public ReferenceMetadataType getReferenceMetadataType();
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.IReferenceMetadataProvider#getReferenceMetadata
     * (edu.asu.diging.vspace.core.references.Reference)
     * throws edu.asu.diging.vspace.core.exception.ReferenceMetadataEncodingException exception
     */
    public String getReferenceMetadata(Reference reference) throws ReferenceMetadataEncodingException;
    
}
