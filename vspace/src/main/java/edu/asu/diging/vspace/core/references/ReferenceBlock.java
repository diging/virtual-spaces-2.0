package edu.asu.diging.vspace.core.references;

import edu.asu.diging.vspace.core.model.impl.Reference;

public class ReferenceBlock {
    
    private Reference reference;
    
    private String refDisplayText;
    
    public ReferenceBlock(Reference ref, String refDisplayText) {
        this.reference = ref;
        this.refDisplayText = refDisplayText;
    }

    public Reference getReference() {
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
