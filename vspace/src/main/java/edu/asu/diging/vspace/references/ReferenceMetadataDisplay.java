package edu.asu.diging.vspace.references;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.vspace.core.model.IReference;

public class ReferenceMetadataDisplay {
    
    @Autowired
    private static ReferenceMetadataRegistry refMetaDataRegistry;
    
    public static String urlEncodedRefMetaData(ReferenceMetaDataType refMetaDataType, IReference ref) {
        return refMetaDataRegistry.getProvider(refMetaDataType).getReferenceMetadata(ref);
    }
    
}
