package edu.asu.diging.vspace.referenceExpose;

import edu.asu.diging.vspace.core.model.IReference;

public class ReferenceMetadataDisplay {
    
    public static String urlEncodedRefMetaData(IReference ref) {
        ReferenceContext refContext = new ReferenceContext(new CitationStyleDefault(), ref); //currently default citation 
        return refContext.executeBiblioMetadata();
    }
}
