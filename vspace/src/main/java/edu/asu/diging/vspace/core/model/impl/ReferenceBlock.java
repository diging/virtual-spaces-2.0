package edu.asu.diging.vspace.core.model.impl;

public class ReferenceBlock {
    
    Reference reference;
    
    String refDisplayText;
    
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
