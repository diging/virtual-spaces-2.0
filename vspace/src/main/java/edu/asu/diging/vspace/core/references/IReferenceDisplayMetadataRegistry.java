package edu.asu.diging.vspace.core.references;

/*
 * (non-Javadoc)
 * 
 * IReferenceMetadataRegistry allows to manage ReferenceMetadataRegistry.
 * 
 * @see  edu.asu.diging.vspace.core.references.IReference
 */

public interface IReferenceDisplayMetadataRegistry {

    public void init();

    public IReferenceDisplayProvider getProvider(ReferenceDisplayType refMetaDataType);

    public IReferenceDisplayProvider getProvider(String displayType);

}
