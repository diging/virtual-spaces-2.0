package edu.asu.diging.vspace.referenceExpose;

import edu.asu.diging.vspace.core.model.IReference;

public class ReferenceContext {
    
    private ICitationStyle citationStyle;
    
    private IReference reference;

    public ReferenceContext(ICitationStyle citationStyle, IReference reference) {
        this.citationStyle = citationStyle;
        this.reference = reference;
    }
    
    public IReferenceMetadata getReferenceMetadataConverter() {
        //We can introduce switch case for different types of metadata/citation style to return appropriate IBiblioMetadata
        //For now its default
        IReferenceMetadata referenceMetadata = new ZoteroDefaultMetaData(citationStyle, reference);
        return referenceMetadata;
    }

    public String  executeBiblioMetadata() {
        IReferenceMetadata referenceMetadata = this.getReferenceMetadataConverter();
        return referenceMetadata.createReferenceMetadata(this.citationStyle, this.reference);
    }
}
