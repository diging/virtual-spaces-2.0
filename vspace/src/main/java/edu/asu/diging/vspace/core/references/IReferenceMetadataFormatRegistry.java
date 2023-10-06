package edu.asu.diging.vspace.core.references;

/*
 * (non-Javadoc)
 * 
 * IReferenceMetadataRegistry allows to manage ReferenceMetadataRegistry.
 * 
 * @see  edu.asu.diging.vspace.core.references.IReference
 */

public interface IReferenceMetadataFormatRegistry {

    public void init();

    public IReferenceMetadataProvider getProvider(ReferenceMetadataType refMetaDataType);

    public IReferenceMetadataProvider getProvider(String metadataType);

}
