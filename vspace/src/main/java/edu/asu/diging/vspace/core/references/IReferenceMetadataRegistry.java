package edu.asu.diging.vspace.core.references;

/**
 *This instance of this interface is responsible for providing the metadata to the references.
 *
 */
public interface IReferenceMetadataRegistry {
    
    public void init();
    
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType);
    
    public IReferenceMetadataProvider getProvider(String metadataType);

}
