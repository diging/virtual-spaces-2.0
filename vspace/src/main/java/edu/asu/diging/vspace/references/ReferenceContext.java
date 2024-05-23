package edu.asu.diging.vspace.references;

import edu.asu.diging.vspace.core.model.IReference;
import edu.asu.diging.vspace.core.model.impl.Reference;

public class ReferenceContext {
    
    private ICitationStyle citationStyle;
    
    private IReference reference;

    public ReferenceContext(ICitationStyle citationStyle, Reference reference) {
        this.citationStyle = citationStyle;
        this.reference = reference;
    }
    
    public IReferenceMetadata getReferenceMetadataConverter() {
        //We can introduce switch case for different types of metadata/citation style to return appropriate IBiblioMetadata
        //For now its default
        IReferenceMetadata referenceMetadata = new ZoteroDefaultMetaData(citationStyle, reference);
        return referenceMetadata;
    }

    public String  executeBiblioMetadata(IReference reference) {
        IReferenceMetadata referenceMetadata = this.getReferenceMetadataConverter();
        return referenceMetadata.createReferenceMetadata(citationStyle, reference);
    }
}
