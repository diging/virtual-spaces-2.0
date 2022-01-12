package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public class ReferenceBlock {
    
    private IReference reference;
    
    private String refDisplayText;
    
    public ReferenceBlock(IReference ref, String refDisplayText) {
        this.reference = ref;
        this.refDisplayText = refDisplayText;
    }

    public IReference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getRefDisplayText() {
        return refDisplayText;
    }

    public void setRefDisplayText(String referenceDisplayText) {
        this.refDisplayText = referenceDisplayText;
    }

}
