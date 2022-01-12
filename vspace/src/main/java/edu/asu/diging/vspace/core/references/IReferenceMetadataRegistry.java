package edu.asu.diging.vspace.core.references;

/**
 * This interface is responsible for getting the registered provider class to bring the metadata of references
 *
 */
public interface IReferenceMetadataRegistry {
    
    public void init();
    
    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType);
    
    public IReferenceMetadataProvider getProvider(String metadataType);

}
