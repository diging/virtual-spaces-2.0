package edu.asu.diging.vspace.references;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceMetadataRegistry {
    
    @Autowired
    private List<ReferenceMetadataProvider> referenceMetadataProviders;
    
    private Map<ReferenceMetaDataType, ReferenceMetadataProvider> map = new HashMap<>();
    
    public void init() {
        for(ReferenceMetadataProvider refMetaData : referenceMetadataProviders) {
            map.put(refMetaData.getReferenceMetadataType(), refMetaData);
        }
    }
    
    public ReferenceMetadataProvider getProvider(ReferenceMetaDataType refMetaDataType) {
        return map.get(refMetaDataType);
    }
}
