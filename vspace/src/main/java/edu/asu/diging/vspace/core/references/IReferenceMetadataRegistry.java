package edu.asu.diging.vspace.core.references;

public interface IReferenceMetadataRegistry {
    
    public void init();
    
    public ReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType);
    
    public ReferenceMetadataProvider getProvider(String metadataType);

}
