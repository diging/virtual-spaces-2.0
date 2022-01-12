package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.model.IReference;

/**
 * This interface is responsible for creating the display text of references over staff and public site 
 *
 */
public interface IReferenceDisplayProvider {
    
    public String getReferenceDisplayText(IReference reference);
    
}
