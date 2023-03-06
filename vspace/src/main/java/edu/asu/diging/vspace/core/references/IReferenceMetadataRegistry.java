package edu.asu.diging.vspace.core.references;


public interface IReferenceMetadataRegistry {
    
    public void init();
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.impl.ReferenceMetadataRegistry#getProvider
     * (edu.asu.diging.vspace.core.references
     * .ReferenceMetadataType)
     */
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType);
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * edu.asu.diging.vspace.core.references.impl.ReferenceMetadataRegistry#getProvider(String metadataType)
     */
    public IReferenceMetadataProvider getProvider(String metadataType);

}
