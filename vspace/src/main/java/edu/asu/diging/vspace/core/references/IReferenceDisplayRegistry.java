package edu.asu.diging.vspace.core.references;

/*
 * (non-Javadoc)
 * 
 * IReferenceDisplayRegistry allows to manage ReferenceDisplayRegistry. 
 * This registry is used to o serve different display styles for references. 
 * The different formats can be (APA, Chicago, etc). 
 * 
 * 
 * @see  edu.asu.diging.vspace.core.references.IReference
 */

public interface IReferenceDisplayRegistry {

    public void init();

    public IReferenceDisplayProvider getProvider(ReferenceDisplayType refDisplayType);

    public IReferenceDisplayProvider getProvider(String displayType);

}
